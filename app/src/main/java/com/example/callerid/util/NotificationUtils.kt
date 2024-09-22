package com.example.callerid.util

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.example.callerid.R
import android.view.Gravity
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.example.callerid.PhoneCallReceiver
import kotlinx.coroutines.Dispatchers

object NotificationUtils {

    var popup = false
    fun showFloatingNotification(
        context: Context,
        callerName: String,
        callerDesignation: String,
        callerNumber: String
    ): View {
       val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        // Inflate your custom layout
        val inflater = LayoutInflater.from(context)
        val overlayView = inflater.inflate(R.layout.popup_window, null)





        // Set data in the overlay
        overlayView.findViewById<TextView>(R.id.callerName).text = callerName
        overlayView.findViewById<TextView>(R.id.callerDesignation).text = callerDesignation
        overlayView.findViewById<TextView>(R.id.callerNumber).text = callerNumber
        overlayView.findViewById<ImageView>(R.id.close).setOnClickListener{
            closeNotification(windowManager, overlayView, 200)
        }

        // Define layout params for the overlay
        val layoutParams = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL or WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED,
            PixelFormat.TRANSLUCENT
        )

        // Position the overlay in the center
        layoutParams.gravity = Gravity.CENTER

        // Add the overlay to the window
        popup = true
        windowManager.addView(overlayView, layoutParams)
        return overlayView
    }

     private fun closeNotification(windowManager: WindowManager, overlayView: View, time: Long) {
         popup = false
         ViewPopup.delete()
        Handler(Looper.getMainLooper()).postDelayed({
            windowManager.removeView(overlayView)
        }, time)
    }


}
