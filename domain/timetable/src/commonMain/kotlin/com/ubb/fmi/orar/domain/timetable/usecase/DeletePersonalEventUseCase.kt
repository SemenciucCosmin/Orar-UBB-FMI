package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.data.notifications.repository.NotificationRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource

/**
 * Use case for deleting a certain event from database
 *
 * The event's scheduled notification is cancelled as well: on Android each alarm re-arms the next
 * occurrence straight from its own intent data, so a deleted event would otherwise keep notifying.
 */
class DeletePersonalEventUseCase(
    private val eventsDataSource: EventsDataSource,
    private val notificationRepository: NotificationRepository,
    private val logger: Logger,
) {
    /**
     * Deletes certain event from database
     */
    suspend operator fun invoke(eventId: String) {
        logger.d(TAG, "Deleting event $eventId and cancelling its notification")
        notificationRepository.cancel(eventId)
        eventsDataSource.deleteEvent(eventId)
    }

    companion object {
        private const val TAG = "DeletePersonalEventUseCase"
    }
}
