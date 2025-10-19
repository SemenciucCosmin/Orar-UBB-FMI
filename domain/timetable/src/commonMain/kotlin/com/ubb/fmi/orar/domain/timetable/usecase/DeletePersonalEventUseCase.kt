package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource

/**
 * Use case for deleting a certain event from database
 */
class DeletePersonalEventUseCase(private val eventsDataSource: EventsDataSource) {
    /**
     * Deletes certain event from database
     */
    suspend operator fun invoke(eventId: String) {
        eventsDataSource.deleteEvent(eventId)
    }
}
