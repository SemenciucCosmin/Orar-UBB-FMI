package com.ubb.fmi.orar.data.timetable.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.EventDao
import com.ubb.fmi.orar.data.database.model.EventEntity
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency

/**
 * Data source for managing all timetable events
 */
class EventsDataSourceImpl(
    private val eventDao: EventDao,
    private val logger: Logger,
) : EventsDataSource {

    /**
     * Retrieve list of [Event] for specific [ownerId] from cache
     * by [configurationId]
     */
    override suspend fun getEventsFromCache(
        configurationId: String,
        ownerId: String,
    ): List<Event> {
        val eventEntities = eventDao.getAllByConfigurationAndOwner(
            configurationId = configurationId,
            ownerId = ownerId
        )

        return eventEntities.map(::mapEntityToEvent)
    }

    /**
     * Saves new list of [Event] to cache
     */
    override suspend fun saveEventsInCache(
        ownerId: String,
        events: List<Event>
    ) {
        val eventEntities = events.map { mapEventToEntity(ownerId, it) }
        eventEntities.forEach { eventDao.insert(it) }
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
        val eventEntity = eventDao.getById(eventId)
        val newEventEntity = eventEntity.copy(isVisible = !eventEntity.isVisible)
        eventDao.insert(newEventEntity)
    }

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate events for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        eventDao.deleteAll(configurationId)
    }

    /**
     * Maps a [Event] to a [EventEntity]
     */
    private fun mapEventToEntity(
        ownerId: String,
        event: Event
    ): EventEntity {
        return EventEntity(
            id = event.id,
            configurationId = event.configurationId,
            ownerId = ownerId,
            dayId = event.day.id,
            frequencyId = event.frequency.id,
            startHour = event.startHour,
            endHour = event.endHour,
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
            day = Day.getById(entity.dayId),
            frequency = Frequency.getById(entity.frequencyId),
            startHour = entity.startHour,
            endHour = entity.endHour,
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
