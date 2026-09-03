package com.ubb.fmi.orar.data.notifications.repository

import Logger
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
import com.ubb.fmi.orar.data.notifications.receiver.buildNotificationIntentUri
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import java.util.Calendar

/**
 * Android [NotificationRepository] implementation, scheduling local notifications as
 * [AlarmManager] alarms that trigger [NotificationReceiver]. Each occurrence reschedules the
 * next one itself from within the receiver (see [NotificationReceiver]), so cancelling relies
 * on rebuilding the exact same [PendingIntent] used at schedule time.
 */
class NotificationRepositoryImpl(
    private val context: Context,
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val logger: Logger,
) : NotificationRepository {

    init {
        createNotificationChannel(context)
    }

    @SuppressLint("MissingPermission")
    override suspend fun schedule(notification: EventNotification) {
        try {
            val triggerMillis = getNotificationTriggerMillis(
                day = notification.day,
                startHour = notification.startHour,
                startMinute = notification.startMinute,
                frequency = notification.frequency,
            )
            notificationCacheDataSource.getNotificationManager().scheduleExact(
                AlarmManager.RTC_WAKEUP,
                triggerMillis,
                buildPendingIntent(notification),
            )
            logger.d(TAG, "Scheduled notification ${notification.id} for $triggerMillis")
        } catch (exception: SecurityException) {
            logger.e(TAG, "Failed to schedule notification ${notification.id}: ${exception.message}")
        }
    }

    override suspend fun cancel(id: String) {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            data = buildNotificationIntentUri(id)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE,
        ) ?: run {
            logger.d(TAG, "Cancel skipped, no pending alarm found for notification $id")
            return
        }
        notificationCacheDataSource.getNotificationManager().cancel(pendingIntent)
        pendingIntent.cancel()
        logger.d(TAG, "Cancelled notification $id")
    }

    /**
     * Builds the [PendingIntent] carrying all data [NotificationReceiver] needs to display the
     * notification and reschedule its next occurrence.
     */
    private fun buildPendingIntent(notification: EventNotification): PendingIntent {
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            // Unique data Uri ensures PendingIntent equality never collides across
            // different notification ids, even if their hashCodes happen to match.
            data = buildNotificationIntentUri(notification.id)
            putExtra(EXTRA_NOTIFICATION_ID, notification.id)
            putExtra(EXTRA_EVENT_ACTIVITY, notification.activity)
            putExtra(EXTRA_EVENT_TYPE_ID, notification.type.id)
            putExtra(EXTRA_EVENT_LOCATION, notification.location)
            putExtra(EXTRA_EVENT_PARTICIPANT, notification.participant)
            putExtra(EXTRA_FREQUENCY_ID, notification.frequency.id)
            putExtra(EXTRA_START_HOUR, notification.startHour)
            putExtra(EXTRA_START_MINUTE, notification.startMinute)
            putExtra(EXTRA_END_HOUR, notification.endHour)
            putExtra(EXTRA_END_MINUTE, notification.endMinute)
        }

        return PendingIntent.getBroadcast(
            context,
            notification.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }

    /**
     * Resolves the next absolute trigger time for [frequency], accounting for the alternating
     * odd/even week pattern of [Frequency.WEEK_1]/[Frequency.WEEK_2] events.
     */
    private fun getNotificationTriggerMillis(
        day: Day,
        startHour: Int,
        startMinute: Int,
        frequency: Frequency,
    ): Long {
        return when (frequency) {
            Frequency.BOTH -> getCalendar(day, startHour, startMinute).timeInMillis

            else -> {
                val calendar = getCalendar(day, startHour, startMinute)
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

    /**
     * Returns the next occurrence of [day]/[hour]:[minute], rolling over to next week if that
     * time has already passed today/this week.
     */
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

    /**
     * Creates the notification channel used for all event notifications, if it doesn't
     * already exist (Android 8.0/API 26+ requires one before any notification can be posted).
     */
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
        private const val TAG = "NotificationRepository"
        private const val CHANNEL_ID = "event_notifications"
        private const val EXTRA_NOTIFICATION_ID = "notification_id"
        private const val EXTRA_EVENT_ACTIVITY = "event_activity"
        private const val EXTRA_EVENT_TYPE_ID = "event_type_id"
        private const val EXTRA_EVENT_LOCATION = "event_location"
        private const val EXTRA_EVENT_PARTICIPANT = "event_participant"
        private const val EXTRA_FREQUENCY_ID = "frequency_id"
        private const val EXTRA_START_HOUR = "start_hour"
        private const val EXTRA_START_MINUTE = "start_minute"
        private const val EXTRA_END_HOUR = "end_hour"
        private const val EXTRA_END_MINUTE = "end_minute"
    }
}
