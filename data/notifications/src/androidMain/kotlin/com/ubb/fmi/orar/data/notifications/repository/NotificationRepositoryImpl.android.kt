package com.ubb.fmi.orar.data.notifications.repository

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSource
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.notifications.model.ClassNotification
import com.ubb.fmi.orar.data.notifications.preferences.NotificationPreferences
import com.ubb.fmi.orar.data.notifications.receiver.NotificationReceiver
import kotlinx.coroutines.flow.first
import java.util.Calendar

class NotificationRepositoryImpl(
    private val context: Context,
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val notificationPreferences: NotificationPreferences
) : NotificationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun schedule(notification: ClassNotification) {
        val triggerMillis = when (notification.frequency) {
            Frequency.BOTH -> nextWeeklyAlarmTime(notification.day, notification.hour, notification.minute)
            else -> nextBiWeeklyAlarmTime(notification.day, notification.hour, notification.minute, notification.frequency)
        }

        val intervalMillis = when (notification.frequency) {
            Frequency.BOTH -> WEEK_IN_MS
            else -> 2 * WEEK_IN_MS
        }

        notificationCacheDataSource.getNotificationManager().setRepeating(
            AlarmManager.RTC_WAKEUP,
            triggerMillis,
            intervalMillis,
            buildPendingIntent(notification),
        )

        notificationPreferences.addScheduledId(notification.id)
        notificationPreferences.setNotificationData(notification.id, serializeNotification(notification))
    }

    override suspend fun cancel(id: String) {
        cancelAlarm(id)
        notificationPreferences.removeScheduledId(id)
        notificationPreferences.removeNotificationData(id)
    }

    override suspend fun cancelAll() {
        notificationPreferences.getScheduledIds().first().forEach { cancelAlarm(it) }
        notificationPreferences.clearScheduledIds()
    }

    override suspend fun getScheduled(): List<ClassNotification> {
        return notificationPreferences.getScheduledIds().first().mapNotNull { id ->
            val raw = notificationPreferences.getNotificationData(id) ?: return@mapNotNull null
            parseNotification(raw)
        }
    }

    private fun buildPendingIntent(notification: ClassNotification): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra(NotificationReceiver.EXTRA_NOTIFICATION_ID, notification.id)
            putExtra(NotificationReceiver.EXTRA_CLASS_NAME, notification.className)
            putExtra(NotificationReceiver.EXTRA_CLASS_TYPE, notification.classType.id)
            putExtra(NotificationReceiver.EXTRA_HOUR, notification.hour)
            putExtra(NotificationReceiver.EXTRA_MINUTE, notification.minute)
        }
        return PendingIntent.getBroadcast(
            context,
            notification.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    private fun cancelAlarm(id: String) {
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

    private fun serializeNotification(n: ClassNotification): String =
        "${n.id}|${n.className}|${n.classType.id}|${n.frequency.id}|${n.day.id}|${n.hour}|${n.minute}"

    private fun parseNotification(raw: String): ClassNotification? {
        val parts = raw.split("|")
        if (parts.size < 7) return null
        return ClassNotification(
            id = parts[0],
            className = parts[1],
            classType = EventType.getById(parts[2]),
            frequency = Frequency.getById(parts[3]),
            day = Day.getById(parts[4]),
            hour = parts[5].toIntOrNull() ?: return null,
            minute = parts[6].toIntOrNull() ?: return null,
        )
    }

    private fun nextWeeklyAlarmTime(day: Day, hour: Int, minute: Int): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, day.toCalendarDayOfWeek())
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (calendar.timeInMillis <= System.currentTimeMillis()) calendar.add(Calendar.WEEK_OF_YEAR, 1)
        return calendar.timeInMillis
    }

    private fun nextBiWeeklyAlarmTime(day: Day, hour: Int, minute: Int, frequency: Frequency): Long {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_WEEK, day.toCalendarDayOfWeek())
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        if (calendar.timeInMillis <= System.currentTimeMillis()) calendar.add(Calendar.WEEK_OF_YEAR, 1)
        val weekNumber = calendar.get(Calendar.WEEK_OF_YEAR)
        val isOddWeek = weekNumber % 2 != 0
        val needsOddWeek = frequency == Frequency.WEEK_1
        if (isOddWeek != needsOddWeek) calendar.add(Calendar.WEEK_OF_YEAR, 1)
        return calendar.timeInMillis
    }

    private fun Day.toCalendarDayOfWeek(): Int = when (this) {
        Day.MONDAY -> Calendar.MONDAY
        Day.TUESDAY -> Calendar.TUESDAY
        Day.WEDNESDAY -> Calendar.WEDNESDAY
        Day.THURSDAY -> Calendar.THURSDAY
        Day.FRIDAY -> Calendar.FRIDAY
        Day.SATURDAY -> Calendar.SATURDAY
        Day.SUNDAY -> Calendar.SUNDAY
    }

    companion object {
        private const val WEEK_IN_MS = 7L * 24 * 60 * 60 * 1000
    }
}
