package com.ubb.fmi.orar.domain.notifications.usecase

import com.ubb.fmi.orar.data.notifications.model.EventNotification
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.model.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class ScheduleEventNotificationsUseCase(
    private val coroutineScope: CoroutineScope,
    private val notificationRepository: NotificationRepository,
) {
    operator fun invoke(vararg events: Event) {
        val eventNotifications = events.map { event ->
            EventNotification(
                id = event.id,
                eventName = event.activity,
                eventType = event.type,
                frequency = event.frequency,
                day = event.day,
                hour = event.startHour.dec(),
                minute = event.endMinute
            )
        }

        coroutineScope.launch {
            eventNotifications.forEach { notification ->
                notificationRepository.schedule(notification)
            }
        }
    }
}
