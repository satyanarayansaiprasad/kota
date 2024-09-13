package com.example.callerid

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object NotificationUtils {

    private const val CHANNEL_ID = "caller_id_channel"

    fun sendNotification(context: Context, name: String, designation: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Incoming Call")
            .setContentText("Caller: $name\nDesignation: $designation")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true) // Dismiss the notification when tapped

        notificationManager.notify(1, builder.build())
    }
}
