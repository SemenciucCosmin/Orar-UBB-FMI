package com.ubb.fmi.orar.data.timetable.datasource

import com.ubb.fmi.orar.data.timetable.model.Event

/**
 * Data source for managing all timetable events
 */
interface EventsDataSource {

    /**
     * Retrieve list of [Event] for specific [ownerId] from cache
     * by [configurationId]
     */
    suspend fun getEventsFromCache(
        configurationId: String,
        ownerId: String,
    ): List<Event>

    /**
     * Saves new list of [Event] to cache
     */
    suspend fun saveEventsInCache(
        ownerId: String,
        events: List<Event>
    )

    /**
     * Sorts [Event] objects by day order index, start hour and end hour
     */
    fun sortEvents(events: List<Event>): List<Event>

    /**
     * Change visibility of specific timetable event by [eventId]
     */
    suspend fun changeEventVisibility(eventId: String)

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    suspend fun invalidate(year: Int, semesterId: String)
}
