package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.EventType

/**
 * Cancels the currently scheduled event notifications and clears the cached
 * [com.ubb.fmi.orar.data.timetable.model.Event.isNotificationOn] flag of every *non-personal*
 * event, since those are re-derived from the network by
 * [InitializeTimetableNotificationsUseCase].
 *
 * Personal events are user-owned and have no network counterpart, so their flag is always
 * preserved — clearing it would lose the user's choice for good, as nothing would ever turn it
 * back on. [forceAll] only decides whether their *scheduled notifications* are cancelled too.
 */
class InvalidateTimetableNotificationsUseCase(
    private val eventsDataSource: EventsDataSource,
    private val notificationRepository: NotificationRepository,
    private val logger: Logger,
) {
    /**
     * @param forceAll when `true`, personal events have their notifications canceled as well.
     * Reserved for muting everything from the notifications settings screen: leaving personal
     * alarms armed there is what previously let them keep firing after the user turned the
     * global notifications switch off, since nothing else ever canceled them. Every other
     * caller re-syncs right afterwards and must leave personal notifications running, since the
     * re-sync only restores the network ones.
     */
    suspend operator fun invoke(forceAll: Boolean = false) {
        val eventsWithNotifications = eventsDataSource.getEventsWithNotificationsOnFromCache()
        val impersonalEvents = eventsWithNotifications.filter { it.type != EventType.PERSONAL }
        val eventsToCancel = if (forceAll) eventsWithNotifications else impersonalEvents

        logger.d(TAG, "Invalidating ${eventsToCancel.size} event notification(s), forceAll: $forceAll")
        eventsToCancel.forEach { event ->
            notificationRepository.cancel(event.id)
        }

        val invalidatedEvents = impersonalEvents.map { it.copy(isNotificationOn = false) }
        invalidatedEvents.groupBy { it.ownerId }.forEach { (ownerId, events) ->
            eventsDataSource.saveEventsInCache(
                ownerId = ownerId,
                events = events
            )
        }
    }

    companion object {
        private const val TAG = "InvalidateTimetableNotificationsUseCase"
    }
}
