package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsParameter

/**
 * Toggles [com.ubb.fmi.orar.data.timetable.model.Event.isNotificationOn] for the event with
 * [eventId] and immediately schedules/cancels its notification to match, so the user's choice
 * takes effect right away instead of waiting for the next app sync.
 */
class ChangeEventNotificationUseCase(
    private val eventsDataSource: EventsDataSource,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val notificationRepository: NotificationRepository,
    private val analyticsLogger: AnalyticsLogger,
    private val logger: Logger,
) {

    suspend operator fun invoke(eventId: String) {
        eventsDataSource.changeEventNotification(eventId)
        val event = eventsDataSource.getEventFromCache(eventId) ?: run {
            logger.e(TAG, "Notification toggled for event $eventId, but it is missing from cache")
            return
        }

        logger.d(TAG, "Notification for event $eventId toggled to ${event.isNotificationOn}")
        analyticsLogger.logEvent(
            event = when {
                event.isNotificationOn -> AnalyticsEvent.EVENT_NOTIFICATION_ON
                else -> AnalyticsEvent.EVENT_NOTIFICATION_OFF
            },
            params = mapOf(AnalyticsParameter.EVENT_TYPE to event.type.id),
        )
        when {
            event.isNotificationOn -> scheduleEventNotificationsUseCase(event)
            else -> notificationRepository.cancel(event.id)
        }
    }

    companion object {
        private const val TAG = "ChangeEventNotificationUseCase"
    }
}
