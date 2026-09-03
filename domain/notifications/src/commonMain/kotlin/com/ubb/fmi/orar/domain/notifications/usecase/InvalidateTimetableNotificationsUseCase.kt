package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.EventType

/**
 * Cancels every currently scheduled non-personal event notification and flips their cached
 * [com.ubb.fmi.orar.data.timetable.model.Event.isNotificationOn] flag back to `false`.
 *
 * Called before [InitializeTimetableNotificationsUseCase] re-syncs from the network, so stale
 * notifications for events that no longer exist/changed don't linger scheduled.
 */
class InvalidateTimetableNotificationsUseCase(
    private val eventsDataSource: EventsDataSource,
    private val notificationRepository: NotificationRepository,
    private val logger: Logger,
) {
    suspend operator fun invoke() {
        val events = eventsDataSource.getEventsWithNotificationsOnFromCache()
        val nonPersonalEvents = events.filter { it.type != EventType.PERSONAL }
        val eventsWithNotifications = nonPersonalEvents.filter { it.isNotificationOn }
        val invalidatedEventsNotifications = eventsWithNotifications.map {
            it.copy(isNotificationOn = false)
        }

        logger.d(TAG, "Invalidating ${invalidatedEventsNotifications.size} event notification(s)")

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

    companion object {
        private const val TAG = "InvalidateTimetableNotificationsUseCase"
    }
}
