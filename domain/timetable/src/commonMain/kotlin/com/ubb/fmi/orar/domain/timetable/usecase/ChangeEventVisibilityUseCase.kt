package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsEvent
import com.ubb.fmi.orar.domain.analytics.model.AnalyticsParameter
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
    private val analyticsLogger: AnalyticsLogger,
    private val logger: Logger,
) {
    /**
     * Changes the visibility of a timetable event based on its ID and owner type.
     */
    suspend operator fun invoke(eventId: String) {
        eventsDataSource.changeEventVisibility(eventId)
        val event = eventsDataSource.getEventFromCache(eventId) ?: run {
            logger.e(TAG, "Visibility changed for event $eventId, but it is missing from cache")
            return
        }

        logger.d(
            TAG,
            "Event $eventId visibility toggled to ${event.isVisible}, notificationOn: ${event.isNotificationOn}",
        )
        analyticsLogger.logEvent(
            event = when {
                event.isVisible -> AnalyticsEvent.SHOW_EVENT
                else -> AnalyticsEvent.HIDE_EVENT
            },
            params = mapOf(AnalyticsParameter.EVENT_TYPE to event.type.id),
        )

        when {
            !event.isVisible && event.isNotificationOn -> {
                logger.d(TAG, "Event $eventId hidden, turning notification off and cancelling it")
                eventsDataSource.changeEventNotification(eventId)
                notificationRepository.cancel(event.id)
            }

            !event.isVisible -> {
                logger.d(TAG, "Event $eventId hidden, cancelling any pending notification")
                notificationRepository.cancel(event.id)
            }

            event.isVisible && !event.isNotificationOn -> {
                logger.d(TAG, "Event $eventId shown, turning notification on and scheduling it")
                eventsDataSource.changeEventNotification(eventId)
                scheduleEventNotificationsUseCase(event)
            }

            event.isNotificationOn -> {
                logger.d(TAG, "Event $eventId shown, scheduling its notification")
                scheduleEventNotificationsUseCase(event)
            }
        }
    }

    companion object {
        private const val TAG = "ChangeEventVisibilityUseCase"
    }
}
