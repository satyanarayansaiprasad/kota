package com.example.callerid;

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.TelephonyManager
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.example.callerid.util.ContactUtils
import com.example.callerid.util.NotificationUtils
import com.example.callerid.util.ViewPopup

class PhoneCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == TelephonyManager.ACTION_PHONE_STATE_CHANGED) {
            val state = intent.getStringExtra(TelephonyManager.EXTRA_STATE)
            val incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)

            // Check if phone is ringing and if the incoming number is available
            if (state == TelephonyManager.EXTRA_STATE_RINGING && incomingNumber != null) {

                ContactUtils.setContacts(context);
                var contact = ContactUtils.findContactByNumber(incomingNumber);

                if(contact==null){
                    contact = Contact("Unknown Caller", "Unknown", incomingNumber)
                }

                if(ViewPopup.get()!=null){
                    updateNotification(ViewPopup.get(), context,contact.name, contact.designation, contact.mobile)
                }else {

                    // Send a notification with the contact details using com.example.callerid.util.NotificationUtils
                    val view = NotificationUtils.showFloatingNotification(
                        context,
                        callerName = contact.name,
                        callerDesignation = contact.designation,
                        callerNumber = contact.mobile
                    )
                    ViewPopup.set(view)
                }
            }
        }
    }
    private fun updateNotification(
        view: View?, context: Context,
        callerName: String,
        callerDesignation: String,
        callerNumber: String
    ){
        view!!.findViewById<TextView>(R.id.callerName).text = callerName
        view!!.findViewById<TextView>(R.id.callerDesignation).text = callerDesignation
        view!!.findViewById<TextView>(R.id.callerNumber).text = callerNumber
    }
}
