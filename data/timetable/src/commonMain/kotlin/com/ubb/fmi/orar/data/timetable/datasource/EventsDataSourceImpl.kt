package com.ubb.fmi.orar.data.timetable.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.EventDao
import com.ubb.fmi.orar.data.database.model.EventEntity
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Data source for managing all timetable events
 */
class EventsDataSourceImpl(
    private val eventDao: EventDao,
    private val logger: Logger,
) : EventsDataSource {

    /**
     * Retrieve list of all [Event] as [Flow] from cache
     */
    override suspend fun getAllEventsFromCache(configurationId: String): Flow<List<Event>> {
        return eventDao.getAllAsFlowByConfiguration(configurationId).map {
            it.map(::mapEntityToEvent)
        }
    }

    /**
     * Retrieve list of [Event] as [Flow] from cache
     */
    override suspend fun getEventsFromCache(
        configurationId: String,
        ownerId: String,
    ): Flow<List<Event>> {
        return eventDao.getAllAsFlowByConfigurationAndOwner(
            configurationId = configurationId,
            ownerId = ownerId
        ).map { it.map(::mapEntityToEvent) }
    }

    /**
     * Retrieve [Event] from cache
     */
    override suspend fun getEventFromCache(
        configurationId: String,
        eventId: String,
    ): Event? {
        val eventEntity = eventDao.getById(eventId)
        return eventEntity?.let(::mapEntityToEvent)
    }

    /**
     * Saves new list of [Event] to cache
     */
    override suspend fun updateEventsInCache(
        configurationId: String,
        ownerId: String,
        events: List<Event>,
    ) {
        val eventEntities = events.map { mapEventToEntity(ownerId, it) }
        eventDao.insertAndUpdateAll(configurationId, ownerId, eventEntities)
    }

    override suspend fun saveEventsInCache(
        ownerId: String,
        events: List<Event>,
    ) {
        val eventEntities = events.map { mapEventToEntity(ownerId, it) }
        eventDao.insertAll(eventEntities)
    }

    override suspend fun saveEventInCache(
        ownerId: String,
        event: Event,
    ) {
        val entity = mapEventToEntity(ownerId, event)
        eventDao.insert(entity)
    }

    /**
     * Sorts [Event] objects by day order index, start hour and end hour
     */
    override fun sortEvents(events: List<Event>): List<Event> {
        return events.sortedWith(
            compareBy<Event> { it.day.orderIndex }
                .thenBy { it.startHour }
                .thenBy { it.endHour }
        )
    }

    /**
     * Change visibility of specific timetable event by [eventId]
     */
    override suspend fun changeEventVisibility(eventId: String) {
        logger.d(TAG, "changeTimetableClassVisibility for eventId: $eventId")
        val eventEntity = eventDao.getById(eventId) ?: return
        val newEventEntity = eventEntity.copy(isVisible = !eventEntity.isVisible)
        eventDao.insert(newEventEntity)
    }

    /**
     * Deletes event with [eventId] from database
     */
    override suspend fun deleteEvent(eventId: String) {
        eventDao.delete(eventId)
    }

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate events for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        eventDao.deleteAllByConfiguration(configurationId)
    }

    /**
     * Maps a [Event] to a [EventEntity]
     */
    private fun mapEventToEntity(
        ownerId: String,
        event: Event,
    ): EventEntity {
        return EventEntity(
            id = event.id,
            configurationId = event.configurationId,
            ownerId = ownerId,
            dayId = event.day.id,
            frequencyId = event.frequency.id,
            startHour = event.startHour,
            startMinute = event.startMinute,
            endHour = event.endHour,
            endMinute = event.endMinute,
            location = event.location,
            activity = event.activity,
            typeId = event.type.id,
            participant = event.participant,
            caption = event.caption,
            details = event.details,
            isVisible = event.isVisible
        )
    }

    /**
     * Maps a [EventEntity] to a [Event]
     */
    private fun mapEntityToEvent(entity: EventEntity): Event {
        return Event(
            id = entity.id,
            configurationId = entity.configurationId,
            ownerId = entity.ownerId,
            day = Day.getById(entity.dayId),
            frequency = Frequency.getById(entity.frequencyId),
            startHour = entity.startHour,
            startMinute = entity.startMinute,
            endHour = entity.endHour,
            endMinute = entity.endMinute,
            location = entity.location,
            activity = entity.activity,
            type = EventType.getById(entity.typeId),
            participant = entity.participant,
            caption = entity.caption,
            details = entity.details,
            isVisible = entity.isVisible,
        )
    }

    companion object {
        private const val TAG = "TimetableEventDataSource"
    }
}
