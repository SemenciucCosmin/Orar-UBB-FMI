package com.ubb.fmi.orar.data.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.ubb.fmi.orar.data.notifications.receiver.NotificationReceiver

fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            NotificationReceiver.CHANNEL_ID,
            "Class Notifications",
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = "Reminders for upcoming university classes"
        }
        val manager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
