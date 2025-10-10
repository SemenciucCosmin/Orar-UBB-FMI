package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource

/**
 * Use case for changing the visibility of a timetable class based on its owner type.
 * This use case interacts with various data sources to perform the visibility change.
 *
 * @property eventsDataSource The data source for timetable events related operations.
 */
class ChangeEventVisibilityUseCase(private val eventsDataSource: EventsDataSource) {
    /**
     * Changes the visibility of a timetable class based on its ID and owner type.
     *
     * @param eventId The ID of the timetable class to change visibility for.
     */
    suspend operator fun invoke(eventId: String) {
        eventsDataSource.changeEventVisibility(eventId)
    }
}
