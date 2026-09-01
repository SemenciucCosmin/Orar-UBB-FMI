package com.ubb.fmi.orar.data.notifications.receiver

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ubb.fmi.orar.data.timetable.model.Frequency

class NotificationReceiver : BroadcastReceiver() {

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra(EXTRA_NOTIFICATION_ID) ?: return
        val className = intent.getStringExtra(EXTRA_CLASS_NAME) ?: return
        val classTypeLabel = intent.getStringExtra(EXTRA_CLASS_TYPE) ?: return
        val frequencyId = intent.getStringExtra(EXTRA_FREQUENCY) ?: return
        val hour = intent.getIntExtra(EXTRA_HOUR, 0)
        val minute = intent.getIntExtra(EXTRA_MINUTE, 0)
        val timeLabel = "%02d:%02d".format(hour, minute)

        val iconRes = context.resources.getIdentifier(
            "ic_app_monochrome",
            "drawable",
            context.packageName
        ).takeIf { it != 0 } ?: context.applicationInfo.icon

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(iconRes)
            .setContentTitle(className)
            .setContentText("$classTypeLabel • $timeLabel")
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id.hashCode(), notification)

        scheduleNext(context, id, className, classTypeLabel, frequencyId, hour, minute)
    }

    @SuppressLint("MissingPermission")
    private fun scheduleNext(
        context: Context,
        id: String,
        className: String,
        classTypeLabel: String,
        frequencyId: String,
        hour: Int,
        minute: Int,
    ) {
        val intervalMillis = if (frequencyId == Frequency.BOTH.id) WEEK_IN_MS else 2 * WEEK_IN_MS
        val nextTrigger = System.currentTimeMillis() + 1 * 60 * 1000L

        val nextIntent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, id)
            putExtra(EXTRA_CLASS_NAME, className)
            putExtra(EXTRA_CLASS_TYPE, classTypeLabel)
            putExtra(EXTRA_FREQUENCY, frequencyId)
            putExtra(EXTRA_HOUR, hour)
            putExtra(EXTRA_MINUTE, minute)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTrigger, pendingIntent)
        } else {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, nextTrigger, pendingIntent)
        }
    }

    companion object {
        private const val CHANNEL_ID = "event_notifications"
        private const val EXTRA_NOTIFICATION_ID = "notification_id"
        private const val EXTRA_CLASS_NAME = "class_name"
        private const val EXTRA_CLASS_TYPE = "class_type"
        private const val EXTRA_FREQUENCY = "frequency"
        private const val EXTRA_HOUR = "hour"
        private const val EXTRA_MINUTE = "minute"
        private const val WEEK_IN_MS = 7L * 24 * 60 * 60 * 1000
    }
}
