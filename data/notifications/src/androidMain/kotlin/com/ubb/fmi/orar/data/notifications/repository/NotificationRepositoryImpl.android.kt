package com.ubb.fmi.orar.data.notifications.repository

import Logger
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.net.toUri
import com.ubb.fmi.orar.data.notifications.datasource.NotificationCacheDataSource
import com.ubb.fmi.orar.data.notifications.manager.scheduleExact
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.receiver.NotificationReceiver
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.calendar.usecase.GetAcademicYearUseCase
import com.ubb.fmi.orar.domain.calendar.usecase.GetUpcomingEventOccurrencesUseCase
import kotlin.time.Clock

/**
 * Android [NotificationRepository] implementation, scheduling local notifications as
 * [AlarmManager] alarms that trigger [NotificationReceiver]. Only the next occurrence is ever
 * armed; each one reschedules the following one from within the receiver (see
 * [NotificationReceiver]), so cancelling relies on rebuilding the exact same [PendingIntent]
 * used at schedule time.
 *
 * The alarm intent is the only state carried between arming an occurrence and the receiver
 * waking up, so it holds the whole [EventNotification]; [getEventNotification] reads it back.
 */
class NotificationRepositoryImpl(
    private val context: Context,
    private val notificationCacheDataSource: NotificationCacheDataSource,
    private val getAcademicYearUseCase: GetAcademicYearUseCase,
    private val getUpcomingEventOccurrencesUseCase: GetUpcomingEventOccurrencesUseCase,
    private val logger: Logger,
) : NotificationRepository {

    init {
        createNotificationChannel(context)
    }

    override suspend fun schedule(notifications: List<EventNotification>) {
        notifications.forEach { notification -> schedule(notification) }
    }

    /**
     * Arms the next occurrence of [notification]. For timetable events it falls inside one of its
     * semester's teaching weeks, and once the semester has none left nothing is armed at all,
     * which is what stops them from firing through the exam sessions and the breaks. Personal
     * events have no semester and always get a next occurrence.
     */
    @SuppressLint("MissingPermission")
    private fun schedule(notification: EventNotification) {
        val triggerMillis = getUpcomingEventOccurrencesUseCase(
            semester = notification.academicSemester,
            day = notification.day,
            frequency = notification.frequency,
            startHour = notification.startHour,
            startMinute = notification.startMinute,
            advanceMinutes = notification.advanceMinutes,
            afterMillis = Clock.System.now().toEpochMilliseconds(),
            limit = 1,
        ).firstOrNull()

        if (triggerMillis == null) {
            logger.d(TAG, "Skipped notification ${notification.id}, no teaching week left to fire in")
            return
        }

        try {
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
            data = buildNotificationUri(id)
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
     * Rebuilds the [EventNotification] stored in an alarm [intent] by [buildPendingIntent].
     * Returns `null` when the intent is missing any required extra.
     *
     * The semester calendar is too large for an intent extra, so it is rebuilt from a moment
     * inside the semester and the semester's index. An intent without a semester belongs to a
     * personal event, which isn't bound to one.
     */
    @Suppress("ReturnCount")
    fun getEventNotification(intent: Intent): EventNotification? {
        val semesterMillis = intent.getLongExtra(EXTRA_SEMESTER_MILLIS, NO_SEMESTER_MILLIS)
        val academicSemester = when (semesterMillis) {
            NO_SEMESTER_MILLIS -> null
            else -> {
                val semesterIndex = intent.getIntExtra(EXTRA_SEMESTER_INDEX, NO_SEMESTER_INDEX)
                getAcademicYearUseCase(semesterMillis).semesters.firstOrNull {
                    it.index == semesterIndex
                } ?: return null
            }
        }

        return EventNotification(
            id = intent.getStringExtra(EXTRA_NOTIFICATION_ID) ?: return null,
            activity = intent.getStringExtra(EXTRA_EVENT_ACTIVITY) ?: return null,
            type = EventType.getById(intent.getStringExtra(EXTRA_EVENT_TYPE_ID) ?: return null),
            location = intent.getStringExtra(EXTRA_EVENT_LOCATION) ?: return null,
            participant = intent.getStringExtra(EXTRA_EVENT_PARTICIPANT) ?: return null,
            frequency = Frequency.getById(intent.getStringExtra(EXTRA_FREQUENCY_ID) ?: return null),
            day = Day.getById(intent.getStringExtra(EXTRA_DAY_ID) ?: return null),
            startHour = intent.getIntExtra(EXTRA_START_HOUR, DEFAULT_TIME),
            startMinute = intent.getIntExtra(EXTRA_START_MINUTE, DEFAULT_TIME),
            endHour = intent.getIntExtra(EXTRA_END_HOUR, DEFAULT_TIME),
            endMinute = intent.getIntExtra(EXTRA_END_MINUTE, DEFAULT_TIME),
            advanceMinutes = intent.getIntExtra(EXTRA_ADVANCE_MINUTES, DEFAULT_TIME),
            academicSemester = academicSemester,
        )
    }

    /**
     * Builds the [PendingIntent] carrying all data [NotificationReceiver] needs to display the
     * notification and schedule its next occurrence.
     *
     * The semester travels as its midpoint rather than its start, so it still resolves to the
     * same academic year even if the device's time zone changes before the alarm fires.
     */
    private fun buildPendingIntent(notification: EventNotification): PendingIntent {
        val semester = notification.academicSemester
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            data = buildNotificationUri(notification.id)
            putExtra(EXTRA_NOTIFICATION_ID, notification.id)
            putExtra(EXTRA_EVENT_ACTIVITY, notification.activity)
            putExtra(EXTRA_EVENT_TYPE_ID, notification.type.id)
            putExtra(EXTRA_EVENT_LOCATION, notification.location)
            putExtra(EXTRA_EVENT_PARTICIPANT, notification.participant)
            putExtra(EXTRA_FREQUENCY_ID, notification.frequency.id)
            putExtra(EXTRA_DAY_ID, notification.day.id)
            putExtra(EXTRA_START_HOUR, notification.startHour)
            putExtra(EXTRA_START_MINUTE, notification.startMinute)
            putExtra(EXTRA_END_HOUR, notification.endHour)
            putExtra(EXTRA_END_MINUTE, notification.endMinute)
            putExtra(EXTRA_ADVANCE_MINUTES, notification.advanceMinutes)
            if (semester != null) {
                putExtra(EXTRA_SEMESTER_MILLIS, (semester.startMillis + semester.endMillis) / 2)
                putExtra(EXTRA_SEMESTER_INDEX, semester.index)
            }
        }

        return PendingIntent.getBroadcast(
            context,
            notification.id.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
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

    /**
     * Builds a stable, unique data [Uri] for a notification's alarm [Intent] so that
     * [PendingIntent] equality never collides between different notification ids, even when
     * their `hashCode()`s happen to match.
     */
    private fun buildNotificationUri(id: String): Uri = "notification://event/$id".toUri()

    companion object {
        private const val TAG = "NotificationRepository"
        internal const val CHANNEL_ID = "event_notifications"
        private const val EXTRA_NOTIFICATION_ID = "notification_id"
        private const val EXTRA_EVENT_ACTIVITY = "event_activity"
        private const val EXTRA_EVENT_TYPE_ID = "event_type_id"
        private const val EXTRA_EVENT_LOCATION = "event_location"
        private const val EXTRA_EVENT_PARTICIPANT = "event_participant"
        private const val EXTRA_FREQUENCY_ID = "frequency_id"
        private const val EXTRA_DAY_ID = "day_id"
        private const val EXTRA_START_HOUR = "start_hour"
        private const val EXTRA_START_MINUTE = "start_minute"
        private const val EXTRA_END_HOUR = "end_hour"
        private const val EXTRA_END_MINUTE = "end_minute"
        private const val EXTRA_ADVANCE_MINUTES = "advance_minutes"
        private const val EXTRA_SEMESTER_MILLIS = "semester_millis"
        private const val EXTRA_SEMESTER_INDEX = "semester_index"
        private const val NO_SEMESTER_MILLIS = Long.MIN_VALUE
        private const val NO_SEMESTER_INDEX = 0
        private const val DEFAULT_TIME = 0
    }
}
