package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.data.timetable.model.Event

/**
 * Maps each of [events] to an [EventNotification] and schedules it via [NotificationRepository],
 * using the user's configured [SettingsPreferences.getNotificationAdvanceMinutes] lead time.
 * Always schedules what it's given; callers decide whether scheduling should happen at all
 * (e.g. only for events with notifications enabled, or while notifications are globally on).
 */
class ScheduleEventNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
    private val settingsPreferences: SettingsPreferences,
    private val logger: Logger,
) {
    suspend operator fun invoke(vararg events: Event) {
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

        logger.d(TAG, "Scheduling ${eventNotifications.size} event notification(s)")
        eventNotifications.forEach { notification ->
            notificationRepository.schedule(notification)
        }
    }

    companion object {
        private const val TAG = "ScheduleEventNotificationsUseCase"
    }
}
