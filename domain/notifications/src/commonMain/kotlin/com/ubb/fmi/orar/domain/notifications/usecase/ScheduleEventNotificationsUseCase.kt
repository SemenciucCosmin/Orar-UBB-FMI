package com.ubb.fmi.orar.domain.notifications.usecase

import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.model.Event

class ScheduleEventNotificationsUseCase(
    private val notificationRepository: NotificationRepository,
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

        eventNotifications.forEach { notification ->
            notificationRepository.schedule(notification)
        }
    }
}
