package com.ubb.fmi.orar.data.timetable.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.EventDao
import com.ubb.fmi.orar.data.database.model.EventEntity
import com.ubb.fmi.orar.data.timetable.model.Activity
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Host
import com.ubb.fmi.orar.data.timetable.model.Location
import com.ubb.fmi.orar.data.timetable.model.Participant

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
    override suspend fun saveEventsInCache(events: List<Event>) {
        val eventEntities = events.map(::mapEventToEntity)
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
    private fun mapEventToEntity(event: Event): EventEntity {
        return EventEntity(
            id = event.id,
            configurationId = event.configurationId,
            ownerId = event.ownerId,
            dayId = event.day.id,
            frequencyId = event.frequency.id,
            startHour = event.startHour,
            endHour = event.endHour,
            locationId = event.location?.id,
            locationName = event.location?.name,
            locationAddress = event.location?.address,
            activityId = event.activity.id,
            activityName = event.activity.name,
            typeId = event.type.id,
            participantId = event.participant?.id,
            participantName = event.participant?.name,
            hostId = event.host?.id,
            hostName = event.host?.name,
            isVisible = event.isVisible
        )
    }

    /**
     * Maps a [EventEntity] to a [Event]
     */
    private fun mapEntityToEvent(entity: EventEntity): Event {
        val location = entity.let {
            Location(
                id = entity.locationId ?: return@let null,
                name = entity.locationName ?: return@let null,
                address = entity.locationAddress ?: return@let null,
            )
        }

        val activity = Activity(
            id = entity.activityId,
            name = entity.activityName
        )

        val participant = entity.let {
            Participant(
                id = it.participantId ?: return@let null,
                name = it.participantName ?: return@let null,
            )
        }

        val host = entity.let {
            Host(
                id = it.hostId ?: return@let null,
                name = it.hostName ?: return@let null,
            )
        }

        return Event(
            id = entity.id,
            configurationId = entity.configurationId,
            ownerId = entity.ownerId,
            day = Day.getById(entity.dayId),
            frequency = Frequency.getById(entity.frequencyId),
            startHour = entity.startHour,
            endHour = entity.endHour,
            location = location,
            activity = activity,
            type = EventType.getById(entity.typeId),
            participant = participant,
            host = host,
            isVisible = entity.isVisible,
        )
    }

    companion object {
        private const val TAG = "TimetableEventDataSource"
    }
}
