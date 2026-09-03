package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.model.Event

/**
 * Maps each of [events] to an [EventNotification] and schedules it via [NotificationRepository].
 * Does not check/update [Event.isNotificationOn] itself; callers are responsible for only
 * passing events that should currently have a notification scheduled.
 */
class ScheduleEventNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke(vararg events: Event) {
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
