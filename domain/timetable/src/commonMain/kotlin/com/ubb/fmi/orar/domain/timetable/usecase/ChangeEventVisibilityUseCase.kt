package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.domain.notifications.usecase.ScheduleEventNotificationsUseCase

/**
 * Use case for changing the visibility of a timetable event based on its owner type.
 * This use case interacts with various data sources to perform the visibility change.
 *
 * Since only visible events should ever have an active notification scheduled, toggling
 * visibility also cancels the event's notification when it becomes hidden, and re-schedules
 * it (if the user had notifications enabled for it) when it becomes visible again.
 */
class ChangeEventVisibilityUseCase(
    private val eventsDataSource: EventsDataSource,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val notificationRepository: NotificationRepository,
) {
    /**
     * Changes the visibility of a timetable event based on its ID and owner type.
     */
    suspend operator fun invoke(eventId: String) {
        eventsDataSource.changeEventVisibility(eventId)
        val event = eventsDataSource.getEventFromCache(eventId) ?: return

        when {
            !event.isVisible && event.isNotificationOn -> {
                eventsDataSource.changeEventNotification(eventId)
                notificationRepository.cancel(event.id)
            }

            !event.isVisible -> notificationRepository.cancel(event.id)

            event.isVisible && !event.isNotificationOn -> {
                eventsDataSource.changeEventNotification(eventId)
                scheduleEventNotificationsUseCase(event)
            }

            event.isNotificationOn -> scheduleEventNotificationsUseCase(event)
        }
    }
}
