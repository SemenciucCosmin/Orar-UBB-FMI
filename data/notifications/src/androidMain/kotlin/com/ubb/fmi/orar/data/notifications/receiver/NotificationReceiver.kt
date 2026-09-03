package com.ubb.fmi.orar.data.notifications.receiver

import Logger
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.extensions.formatTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

/**
 * Builds a stable, unique data [Uri] for a notification's alarm [Intent] so that
 * [PendingIntent] equality never collides between different notification ids,
 * even when their `hashCode()`s happen to match.
 */
fun buildNotificationIntentUri(id: String): Uri = Uri.parse("notification://event/$id")

/**
 * Receives the [AlarmManager] alarm scheduled by [com.ubb.fmi.orar.data.notifications.repository.NotificationRepositoryImpl]
 * for a single event occurrence: displays the notification, then immediately reschedules the
 * next occurrence itself (weekly or bi-weekly, based on [Frequency]). If this reschedule step
 * ever fails or doesn't run (e.g. the receiver never fires because the alarm was dropped by the
 * OS/OEM battery optimizations), no future occurrence gets scheduled for this event until the
 * app is reopened and re-syncs notifications.
 */
class NotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val logger: Logger by inject()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getStringExtra(EXTRA_NOTIFICATION_ID) ?: return
        val activity = intent.getStringExtra(EXTRA_EVENT_ACTIVITY) ?: return
        val eventTypeId = intent.getStringExtra(EXTRA_EVENT_TYPE_ID) ?: return
        val location = intent.getStringExtra(EXTRA_EVENT_LOCATION) ?: return
        val participant = intent.getStringExtra(EXTRA_EVENT_PARTICIPANT) ?: return
        val frequencyId = intent.getStringExtra(EXTRA_FREQUENCY_ID) ?: return
        val startHour = intent.getIntExtra(EXTRA_START_HOUR, DEFAULT_TIME)
        val startMinute = intent.getIntExtra(EXTRA_START_MINUTE, DEFAULT_TIME)
        val endHour = intent.getIntExtra(EXTRA_END_HOUR, DEFAULT_TIME)
        val endMinute = intent.getIntExtra(EXTRA_END_MINUTE, DEFAULT_TIME)
        val startTime = formatTime(startHour, startMinute)
        val endTime = formatTime(endHour, endMinute)
        val eventTypeLabel = getEventTypeLabel(context, eventTypeId)

        val iconRes = context.resources.getIdentifier(
            ICON_RESOURCE_ID,
            ICON_RESOURCE_TYPE,
            context.packageName
        ).takeIf { it != 0 } ?: context.applicationInfo.icon

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(iconRes)
            .setContentTitle("$activity • $startTime - $endTime")
            .setContentText("$eventTypeLabel • $participant • $location")
            .setAutoCancel(true)
            .build()

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.notify(id.hashCode(), notification)
        logger.d(TAG, "Displayed notification $id, rescheduling next occurrence")
        scheduleNext(
            context = context,
            id = id,
            activity = activity,
            eventTypeId = eventTypeId,
            location = location,
            participant = participant,
            frequencyId = frequencyId,
            startHour = startHour,
            startMinute = startMinute,
            endHour = endHour,
            endMinute = endMinute,
        )
    }

    /**
     * Reschedules the next occurrence of this event's notification: one week later for weekly
     * ([Frequency.BOTH]) events, or two weeks later for alternating-week events.
     */
    @SuppressLint("MissingPermission")
    private fun scheduleNext(
        context: Context,
        id: String,
        activity: String,
        eventTypeId: String,
        location: String,
        participant: String,
        frequencyId: String,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
    ) {
        val intervalMillis = if (frequencyId == Frequency.BOTH.id) WEEK_IN_MS else BI_WEEKLY_IN_MS
        val nextIntent = Intent(context, NotificationReceiver::class.java).apply {
            data = buildNotificationIntentUri(id)
            putExtra(EXTRA_NOTIFICATION_ID, id)
            putExtra(EXTRA_EVENT_ACTIVITY, activity)
            putExtra(EXTRA_EVENT_TYPE_ID, eventTypeId)
            putExtra(EXTRA_EVENT_LOCATION, location)
            putExtra(EXTRA_EVENT_PARTICIPANT, participant)
            putExtra(EXTRA_FREQUENCY_ID, frequencyId)
            putExtra(EXTRA_START_HOUR, startHour)
            putExtra(EXTRA_START_MINUTE, startMinute)
            putExtra(EXTRA_END_HOUR, endHour)
            putExtra(EXTRA_END_MINUTE, endMinute)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            id.hashCode(),
            nextIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarmManager.canScheduleExactAlarms()) {
                alarmManager.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, intervalMillis, pendingIntent)
            } else {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    intervalMillis,
                    pendingIntent
                )
            }
        } catch (exception: SecurityException) {
            // Chain broken here: without this alarm, the notification will never
            // fire again for this event until the app is reopened and re-syncs.
            logger.e(TAG, "Failed to schedule next occurrence for notification $id: ${exception.message}")
        }
    }

    /**
     * Returns the localized display label for [eventTypeId] (e.g. "Lecture", "Seminary"),
     * falling back to a title-cased enum name if the string resource can't be resolved.
     */
    private fun getEventTypeLabel(context: Context, eventTypeId: String): String {
        val eventType = EventType.getById(eventTypeId)
        val resourceId = when (eventType) {
            EventType.LECTURE -> LECTURE_RESOURCE_ID
            EventType.SEMINARY -> SEMINARY_RESOURCE_ID
            EventType.LABORATORY -> LABORATORY_RESOURCE_ID
            EventType.STAFF -> STAFF_RESOURCE_ID
            EventType.PERSONAL -> PERSONAL_RESOURCE_ID
        }

        val labelResId = context.resources.getIdentifier(
            resourceId,
            LABEL_RESOURCE_TYPE,
            context.packageName
        )

        return when {
            labelResId != 0 -> context.getString(labelResId)
            else -> eventType.name.lowercase().replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
        }
    }

    companion object {
        private const val TAG = "NotificationReceiver"
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
        private const val WEEK_IN_MS = 7 * 24 * 60 * 60 * 1000L
        private const val BI_WEEKLY_IN_MS = 2 * WEEK_IN_MS
        private const val DEFAULT_TIME = 0
        private const val ICON_RESOURCE_ID = "ic_app_monochrome"
        private const val ICON_RESOURCE_TYPE = "drawable"
        private const val LABEL_RESOURCE_TYPE = "string"
        private const val LECTURE_RESOURCE_ID = "lbl_lecture"
        private const val SEMINARY_RESOURCE_ID = "lbl_seminary"
        private const val LABORATORY_RESOURCE_ID = "lbl_laboratory"
        private const val STAFF_RESOURCE_ID = "lbl_staff"
        private const val PERSONAL_RESOURCE_ID = "lbl_personal"
    }
}
