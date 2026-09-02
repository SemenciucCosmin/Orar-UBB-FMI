package com.ubb.fmi.orar.domain.notifications.usecase

import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource

class ChangeEventNotificationUseCase(
    private val eventsDataSource: EventsDataSource,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val notificationRepository: NotificationRepository,
) {

    suspend operator fun invoke(eventId: String) {
        eventsDataSource.changeEventNotification(eventId)
        val event = eventsDataSource.getEventFromCache(eventId) ?: return

        when {
            event.isNotificationOn -> scheduleEventNotificationsUseCase(event)
            else -> notificationRepository.cancel(event.id)
        }
    }
}
