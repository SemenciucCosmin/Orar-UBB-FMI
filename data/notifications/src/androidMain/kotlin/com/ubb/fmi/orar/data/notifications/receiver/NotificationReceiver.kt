package com.ubb.fmi.orar.data.notifications.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class NotificationReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra(EXTRA_NOTIFICATION_ID) ?: return
        val className = intent.getStringExtra(EXTRA_CLASS_NAME) ?: return
        val classTypeLabel = intent.getStringExtra(EXTRA_CLASS_TYPE) ?: return
        val hour = intent.getIntExtra(EXTRA_HOUR, 0)
        val minute = intent.getIntExtra(EXTRA_MINUTE, 0)

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val dismissed = prefs.getStringSet(KEY_DISMISSED_IDS, emptySet()) ?: emptySet()
        if (id in dismissed) return

        val timeLabel = "%02d:%02d".format(hour, minute)

        // TODO: Replace android.R.drawable.ic_dialog_info with the actual app icon resource
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(className)
            .setContentText("$classTypeLabel • $timeLabel")
            .setAutoCancel(true)
            .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id.hashCode(), notification)
    }

    companion object {
        const val PREFS_NAME = "notifications_prefs"
        const val KEY_DISMISSED_IDS = "dismissed_ids"
        const val CHANNEL_ID = "class_notifications"

        const val EXTRA_NOTIFICATION_ID = "notification_id"
        const val EXTRA_CLASS_NAME = "class_name"
        const val EXTRA_CLASS_TYPE = "class_type"
        const val EXTRA_HOUR = "hour"
        const val EXTRA_MINUTE = "minute"
    }
}
