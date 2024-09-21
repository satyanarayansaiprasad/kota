package com.example.callerid

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContracts
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.InputStream

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check for permissions
        requestPermissions()
        showMinimizeAppDialog()


    }

    // Request permissions for reading phone state and call logs
    private fun requestPermissions() {
        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
            checkSelfPermission(Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED
        ) {

            val requestPermissionLauncher =
                registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                    val phoneStateGranted =
                        permissions[Manifest.permission.READ_PHONE_STATE] ?: false
                    val callLogGranted = permissions[Manifest.permission.READ_CALL_LOG] ?: false
                    if (!phoneStateGranted || !callLogGranted) {
                        // Handle permission denial
                    }
                }

            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_CALL_LOG
                )
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.packageName)
                )

                AlertDialog.Builder(this)
                    .setTitle("Permission Required")
                    .setMessage("This app requires permission to display over other apps in order to show call popups. Please enable it in settings.")
                    .setPositiveButton("Go to Settings") { _, _ ->
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel", null)
                    .show()
            }
        }
    }
    private fun showMinimizeAppDialog() {
        AlertDialog.Builder(this)
            .setTitle("Please Minimize the App or close it.")
            .setMessage("There is nothing here, you can either minimize it or close the app, we will let you know who's calling")
            .setPositiveButton("Okay") { dialog, _ ->
                dialog.dismiss()
                moveTaskToBack(true)
            }
            .setCancelable(false)  // Prevent dismissing by tapping outside the dialog
            .show()
    }
}
