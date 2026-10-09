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
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.net.toUri
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepositoryImpl
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.domain.extensions.formatTime
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.Locale

/**
 * Receives the [AlarmManager] alarm scheduled by
 * [com.ubb.fmi.orar.data.notifications.repository.NotificationRepositoryImpl] for a single event
 * occurrence: displays the notification, then immediately schedules the next one.
 *
 * Rescheduling is delegated straight back to [NotificationRepositoryImpl] with the event rebuilt
 * from the alarm intent, so the "which teaching week comes next" decision lives in exactly one
 * place and the receiver can't drift away from it. If this step ever fails or doesn't run (e.g.
 * the alarm was dropped by OEM battery optimizations), no future occurrence gets scheduled for
 * this event until the app is reopened and re-syncs notifications.
 */
class NotificationReceiver : BroadcastReceiver(), KoinComponent {

    private val logger: Logger by inject()
    private val notificationRepository: NotificationRepositoryImpl by inject()

    @SuppressLint("MissingPermission")
    override fun onReceive(context: Context, intent: Intent) {
        logger.d(TAG, "Alarm received for ${intent.data}")
        val notification = notificationRepository.getEventNotification(intent) ?: run {
            logger.e(TAG, "Dropped alarm ${intent.data}, intent is missing event notification data")
            return
        }

        display(context, notification)
        scheduleNext(notification)
    }

    /** Posts the visible notification for the occurrence this alarm was armed for. */
    @SuppressLint("MissingPermission")
    private fun display(context: Context, notification: EventNotification) {
        val startTime = formatTime(notification.startHour, notification.startMinute)
        val endTime = formatTime(notification.endHour, notification.endMinute)
        val eventTypeLabel = getEventTypeLabel(context, notification.type)

        val iconRes = context.resources.getIdentifier(
            ICON_RESOURCE_ID,
            ICON_RESOURCE_TYPE,
            context.packageName
        ).takeIf { it != 0 } ?: run {
            logger.e(TAG, "Icon $ICON_RESOURCE_ID not found, falling back to the app icon")
            context.applicationInfo.icon
        }

        val deepLinkUri = "$USER_TIMETABLE_DEEP_LINK_BASE?eventId=${Uri.encode(notification.id)}".toUri()
        val deeplinkIntent = Intent(Intent.ACTION_VIEW, deepLinkUri).apply {
            setPackage(context.packageName)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notification.id.hashCode(),
            deeplinkIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val displayedNotification = NotificationCompat.Builder(context, NotificationRepositoryImpl.CHANNEL_ID)
            .setSmallIcon(iconRes)
            .setContentTitle("${notification.activity} • $startTime - $endTime")
            .setContentText("$eventTypeLabel • ${notification.participant} • ${notification.location}")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val notificationManager = context.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        if (!NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            logger.e(TAG, "Notification ${notification.id} will be dropped, app notifications are disabled")
        }

        notificationManager.notify(notification.id.hashCode(), displayedNotification)
        logger.d(TAG, "Displayed notification ${notification.id} (${notification.activity}, ${notification.day.id})")
    }

    /**
     * Arms the occurrence following the one that just fired, keeping the alarm chain alive.
     *
     * [goAsync] holds the broadcast open while the suspending scheduling call runs, since a
     * receiver's process may be killed as soon as [onReceive] returns.
     */
    @Suppress("TooGenericExceptionCaught")
    private fun scheduleNext(notification: EventNotification) {
        val pendingResult = goAsync()
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            try {
                logger.d(TAG, "Scheduling next occurrence of ${notification.id}")
                notificationRepository.schedule(listOf(notification))
            } catch (exception: Exception) {
                logger.e(
                    TAG,
                    "Failed to schedule next occurrence of ${notification.id}, chain broken until next app " +
                        "launch: ${exception.stackTraceToString()}",
                )
            } finally {
                pendingResult.finish()
            }
        }
    }

    /**
     * Returns the localized display label for [eventType] (e.g. "Lecture", "Seminary"),
     * falling back to a title-cased enum name if the string resource can't be resolved.
     */
    private fun getEventTypeLabel(context: Context, eventType: EventType): String {
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
        private const val ICON_RESOURCE_ID = "ic_app_monochrome"
        private const val ICON_RESOURCE_TYPE = "drawable"
        private const val LABEL_RESOURCE_TYPE = "string"
        private const val LECTURE_RESOURCE_ID = "lbl_lecture"
        private const val SEMINARY_RESOURCE_ID = "lbl_seminary"
        private const val LABORATORY_RESOURCE_ID = "lbl_laboratory"
        private const val STAFF_RESOURCE_ID = "lbl_staff"
        private const val PERSONAL_RESOURCE_ID = "lbl_personal"
        private const val USER_TIMETABLE_DEEP_LINK_BASE = "orarubbfmi://user-timetable"
    }
}
