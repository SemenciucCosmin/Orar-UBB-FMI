package com.ubb.fmi.orar.data.timetable.datasource

import com.ubb.fmi.orar.data.timetable.model.Event
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing all timetable events
 */
interface EventsDataSource {

    /**
     * Retrieve list of [Event] as [Flow] from cache
     */
    suspend fun getEventsFromCache(
        configurationId: String,
        ownerId: String,
    ): Flow<List<Event>>

    /**
     * Retrieve [Event] from cache
     */
    suspend fun getEventFromCache(
        configurationId: String,
        eventId: String,
    ): Event?

    /**
     * Saves new list of [Event] to cache
     */
    suspend fun saveEventsInCache(
        ownerId: String,
        events: List<Event>
    )

    /**
     * Saves new [Event] to cache
     */
    suspend fun saveEventInCache(
        ownerId: String,
        event: Event
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
     * Deletes event with [eventId] from database
     */
    suspend fun deleteEvent(eventId: String)

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    suspend fun invalidate(year: Int, semesterId: String)
}
