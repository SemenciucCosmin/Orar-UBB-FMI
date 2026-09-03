package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource

/**
 * Toggles [com.ubb.fmi.orar.data.timetable.model.Event.isNotificationOn] for the event with
 * [eventId] and immediately schedules/cancels its notification to match, so the user's choice
 * takes effect right away instead of waiting for the next app sync.
 */
class ChangeEventNotificationUseCase(
    private val eventsDataSource: EventsDataSource,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val notificationRepository: NotificationRepository,
    private val logger: Logger,
) {

    suspend operator fun invoke(eventId: String) {
        eventsDataSource.changeEventNotification(eventId)
        val event = eventsDataSource.getEventFromCache(eventId) ?: return

        logger.d(TAG, "Notification for event $eventId toggled to ${event.isNotificationOn}")
        when {
            event.isNotificationOn -> scheduleEventNotificationsUseCase(event)
            else -> notificationRepository.cancel(event.id)
        }
    }

    companion object {
        private const val TAG = "ChangeEventNotificationUseCase"
    }
}
