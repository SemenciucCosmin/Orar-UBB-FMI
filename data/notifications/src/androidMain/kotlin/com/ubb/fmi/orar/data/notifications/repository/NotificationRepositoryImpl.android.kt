package com.ubb.fmi.orar.data.notifications.repository

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSource
import com.ubb.fmi.orar.data.notifications.manager.scheduleExact
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.receiver.NotificationReceiver
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import java.util.Calendar

class NotificationRepositoryImpl(
    private val context: Context,
    private val notificationCacheDataSource: NotificationCacheDataSource,
) : NotificationRepository {

    init {
        createNotificationChannel(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun schedule(notification: EventNotification) {
        val triggerMillis = getNotificationTriggerMillis(
            day = notification.day,
            hour = notification.hour,
            minute = notification.minute,
            frequency = notification.frequency,
        )

        notificationCacheDataSource.getNotificationManager().scheduleExact(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            buildPendingIntent(notification),
        )
    }

    override suspend fun cancel(id: String) {
        val intent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
        ) ?: return
        notificationCacheDataSource.getNotificationManager().cancel(pendingIntent)
        pendingIntent.cancel()
    }

    private fun buildPendingIntent(notification: EventNotification): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(EXTRA_NOTIFICATION_ID, notification.id)
            putExtra(EXTRA_CLASS_NAME, notification.eventName)
            putExtra(EXTRA_CLASS_TYPE, notification.eventType.id)
            putExtra(EXTRA_FREQUENCY, notification.frequency.id)
            putExtra(EXTRA_HOUR, notification.hour)
            putExtra(EXTRA_MINUTE, notification.minute)
        }

        return PendingIntent.getBroadcast(
            context,
            notification.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun getNotificationTriggerMillis(
        day: Day,
        hour: Int,
        minute: Int,
        frequency: Frequency,
    ): Long {
        return when (frequency) {
            Frequency.BOTH -> getCalendar(day, hour, minute).timeInMillis

            else -> {
                val calendar = getCalendar(day, hour, minute)
                val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
                val isOddWeek = weekNumber % 2 != 0
                val needsOddWeek = frequency == Frequency.WEEK_1

                if (isOddWeek != needsOddWeek) {
                    calendar.add(Calendar.WEEK_OF_YEAR, 1)
                }

                calendar.timeInMillis
            }
        }
    }

    private fun getCalendar(day: Day, hour: Int, minute: Int): Calendar {
        val calendarDay = when (day) {
            Day.MONDAY -> Calendar.MONDAY
            Day.TUESDAY -> Calendar.TUESDAY
            Day.WEDNESDAY -> Calendar.WEDNESDAY
            Day.THURSDAY -> Calendar.THURSDAY
            Day.FRIDAY -> Calendar.FRIDAY
            Day.SATURDAY -> Calendar.SATURDAY
            Day.SUNDAY -> Calendar.SUNDAY
        }

        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, calendarDay)
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar.add(Calendar.WEEK_OF_YEAR, 1)
        }

        return calendar
    }

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val manager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (manager.getNotificationChannel(CHANNEL_ID) != null) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Class Notifications",
            NotificationManager.IMPORTANCE_DEFAULT,
        )

        manager.createNotificationChannel(channel)
    }

    companion object {
        private const val CHANNEL_ID = "event_notifications"
        private const val EXTRA_NOTIFICATION_ID = "notification_id"
        private const val EXTRA_CLASS_NAME = "class_name"
        private const val EXTRA_CLASS_TYPE = "class_type"
        private const val EXTRA_FREQUENCY = "frequency"
        private const val EXTRA_HOUR = "hour"
        private const val EXTRA_MINUTE = "minute"
    }
}
