package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource

/**
 * Use case for changing the visibility of a timetable event based on its owner type.
 * This use case interacts with various data sources to perform the visibility change.
 */
class ChangeEventVisibilityUseCase(private val eventsDataSource: EventsDataSource) {
    /**
     * Changes the visibility of a timetable event based on its ID and owner type.
     */
    suspend operator fun invoke(eventId: String) {
        eventsDataSource.changeEventVisibility(eventId)
    }
}
