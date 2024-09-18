package com.example.callerid

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object NotificationUtils {

    private const val CHANNEL_ID = "caller_id_channel"

    fun sendNotification(context: Context, name: String, designation: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Ensure notification channel is created for Android 8.0 and above
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Incoming Call Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Incoming Call")
            .setContentText("Caller: $name\nDesignation: $designation")
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Set priority to HIGH to ensure visibility
            .setAutoCancel(true) // Dismiss the notification when tapped
            .setSmallIcon(android.R.drawable.sym_call_incoming)

        notificationManager.notify(1, builder.build())
    }
}
