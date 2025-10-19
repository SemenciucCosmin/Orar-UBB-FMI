package com.ubb.fmi.orar.data.rooms.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Repository for managing data flow and data source for rooms
 */
class RoomsRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val roomsDataSource: RoomsDataSource,
    private val eventsDataSource: EventsDataSource,
    private val timetablePreferences: TimetablePreferences,
) : RoomsRepository {

    private val roomsFlow: MutableStateFlow<Resource<List<Owner.Room>>> = MutableStateFlow(
        Resource(null, Status.Loading)
    )

    private val timetableFlows: MutableMap<String, MutableStateFlow<Resource<Timetable<Owner.Room>>>> =
        mutableMapOf()

    init {
        prefetchRooms()
        initializeRooms()
    }

    /**
     * Retrieves a [Flow] of rooms
     */
    override fun getRooms(): Flow<Resource<List<Owner.Room>>> {
        return roomsFlow.map { resource ->
            val sortedRooms = resource.payload?.sortedBy { it.name }
            resource.copy(payload = sortedRooms)
        }
    }

    /**
     * Retrieves a [Flow] of timetable for certain room
     */
    override fun getTimetable(roomId: String): Flow<Resource<Timetable<Owner.Room>>> {
        if (!timetableFlows.keys.contains(roomId)) {
            timetableFlows[roomId] = MutableStateFlow(Resource(null, Status.Loading))
            prefetchEvents(roomId)
            initializeEvents(roomId)
        }

        return timetableFlows[roomId] ?: MutableStateFlow(Resource(null, Status.NotFoundError))
    }

    /**
     * Invalidates rooms cache
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        roomsDataSource.invalidate(year, semesterId)
    }

    /**
     * Tries prefetching the rooms from API for a safety update of local data
     */
    private fun prefetchRooms() {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let { getRoomsFromApi(it.year, it.semesterId) }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeRooms() {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest
                getRoomsFromCache(configuration.year, configuration.semesterId)
            }
        }
    }

    /**
     * Provides the collection of database data flow
     */
    private suspend fun getRoomsFromCache(
        year: Int,
        semesterId: String,
    ) {
        roomsDataSource.getRoomsFromCache(year, semesterId).collectLatest { rooms ->
            when {
                rooms.isEmpty() -> getRoomsFromApi(year, semesterId)
                else -> roomsFlow.update { Resource(rooms, Status.Success) }
            }
        }
    }

    /**
     * Retrieves rooms from API and update the database of output flow
     */
    private suspend fun getRoomsFromApi(
        year: Int,
        semesterId: String,
    ) {
        val resource = roomsDataSource.getRoomsFromApi(year, semesterId)
        val rooms = resource.payload

        when {
            resource.status.isSuccess() && rooms != null -> {
                roomsDataSource.saveRoomsInCache(rooms)
            }

            else -> roomsFlow.update {
                when {
                    it.payload.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it
                }
            }
        }
    }

    /**
     * Tries prefetching the room events from API for a safety update of local data
     */
    private fun prefetchEvents(roomId: String) {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let {
                val room = roomsDataSource.getRoomsFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { room ->
                    room.id == roomId
                } ?: return@let

                getEventsFromApi(it.year, it.semesterId, room)
            }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeEvents(roomId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest

                val room = roomsDataSource.getRoomsFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { it.id == roomId } ?: return@collectLatest

                getEventsFromCache(configuration.year, configuration.semesterId, room)
            }
        }
    }

    /**
     * Provides the collection of database data flow
     */
    private suspend fun getEventsFromCache(
        year: Int,
        semesterId: String,
        room: Owner.Room,
    ) {
        val configurationId = year.toString() + semesterId
        eventsDataSource.getEventsFromCache(configurationId, room.id).collectLatest { events ->
            when {
                events.isEmpty() -> getEventsFromApi(year, semesterId, room)
                else -> timetableFlows[room.id]?.update {
                    Resource(Timetable(room, events), Status.Success)
                }
            }
        }
    }

    /**
     * Retrieves room events from API and update the database of output flow
     */
    private suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        room: Owner.Room,
    ) {
        val resource = roomsDataSource.getEventsFromApi(year, semesterId, room)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.saveEventsInCache(room.id, events)
            }

            else -> timetableFlows[room.id]?.update {
                when {
                    it.payload?.events.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it
                }
            }
        }
    }
}