package com.example.callerid

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.util.Log

class PhoneCallReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {
                Log.d("PhoneCallReceiver", "Incoming number: $incomingNumber")
                // Trigger a method to handle the incoming number and display the contact info.
                val mainActivityIntent = Intent(context, MainActivity::class.java)
                mainActivityIntent.putExtra("INCOMING_NUMBER", incomingNumber)
                mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context?.startActivity(mainActivityIntent)
            }
        }
    }
}
