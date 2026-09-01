package com.ubb.fmi.orar.domain.notifications.usecase

import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.EventType

class InvalidateTimetableNotificationsUseCase(
    private val eventsDataSource: EventsDataSource,
    private val notificationRepository: NotificationRepository,
) {
    suspend operator fun invoke() {
        val events = eventsDataSource.getAllEventsFromCache()
        val nonPersonalEvents = events.filter { it.type != EventType.PERSONAL }
        val eventsWithNotifications = nonPersonalEvents.filter { it.isNotificationOn }
        val invalidatedEventsNotifications = eventsWithNotifications.map {
            it.copy(isNotificationOn = false)
        }

        val groupedEvents = invalidatedEventsNotifications.groupBy { it.ownerId }
        groupedEvents.forEach { (ownerId, events) ->
            eventsDataSource.saveEventsInCache(
                ownerId = ownerId,
                events = events
            )
        }

        invalidatedEventsNotifications.forEach { event ->
            notificationRepository.cancel(event.id)
        }
    }
}
