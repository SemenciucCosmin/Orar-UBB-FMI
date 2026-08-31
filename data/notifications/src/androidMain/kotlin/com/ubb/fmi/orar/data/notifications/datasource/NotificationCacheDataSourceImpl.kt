package com.ubb.fmi.orar.data.notifications.datasource

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.ubb.fmi.orar.data.notifications.createNotificationChannel
import com.ubb.fmi.orar.data.notifications.receiver.NotificationReceiver

class NotificationCacheDataSourceImpl(
    private val context: Context
): NotificationCacheDataSource {

    private val alarmManager by lazy {
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    init {
        createNotificationChannel(context)
    }

    override fun getNotificationManager(): AlarmManager {
        return alarmManager
    }

    private fun createNotificationChannel(context: Context) {
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
}
