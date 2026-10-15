package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.data.timetable.model.Event

/**
 * Maps each of [events] to an [EventNotification] and schedules it via [NotificationRepository],
 * using the user's configured [SettingsPreferences.getNotificationAdvanceMinutes] lead time.
 *
 * This is the single choke point every scheduling path goes through, so it is also where the
 * global [SettingsPreferences.isNotificationsEnabled] switch is enforced: while notifications are
 * globally off nothing is scheduled, no matter which caller asks. Callers remain responsible for
 * the per-event decision (e.g. only events that are visible and have notifications enabled).
 */
class ScheduleEventNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
    private val settingsPreferences: SettingsPreferences,
    private val logger: Logger,
) {
    suspend operator fun invoke(vararg events: Event) {
        if (events.isEmpty()) return

        if (!settingsPreferences.isNotificationsEnabled()) {
            logger.d(TAG, "Skipped scheduling ${events.size} event(s), notifications are globally off")
            return
        }

        val advanceMinutes = settingsPreferences.getNotificationAdvanceMinutes()
        val eventNotifications = events.map { event ->
            EventNotification(
                id = event.id,
                activity = event.activity,
                type = event.type,
                location = event.location,
                participant = event.participant,
                frequency = event.frequency,
                day = event.day,
                startHour = event.startHour,
                startMinute = event.startMinute,
                endHour = event.endHour,
                endMinute = event.endMinute,
                advanceMinutes = advanceMinutes,
            )
        }

        logger.d(
            TAG,
            "Scheduling ${eventNotifications.size} event notification(s): " +
                eventNotifications.joinToString { it.id },
        )
        notificationRepository.schedule(eventNotifications)
    }

    companion object {
        private const val TAG = "ScheduleEventNotificationsUseCase"
    }
}
