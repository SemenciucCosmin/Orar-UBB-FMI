package com.ubb.fmi.orar.data.rooms.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.network.model.isNotFoundError
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getAllRoomsAndEvents(): Flow<Pair<List<Owner.Room>, List<Event>>> {
        return roomsFlow.transformLatest { resource ->
            val configurationFlow = timetablePreferences.getConfiguration()
            val configuration = configurationFlow.firstOrNull() ?: return@transformLatest
            val configurationId = configuration.year.toString() + configuration.semesterId
            val rooms = resource.payload ?: emptyList()
            prefetchAll(configuration.year, configuration.semesterId, rooms)

            eventsDataSource.getAllEventsFromCacheAsFlow(configurationId).collectLatest { events ->
                emit(rooms to events)
            }
        }.distinctUntilChanged()
    }

    /**
     * Invalidates rooms cache
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        roomsDataSource.invalidate(year, semesterId)
    }

    /**
     * Fetches the rooms from API again, to recover from a failed fetch
     */
    override fun refresh() {
        roomsFlow.update { Resource(it.payload, Status.Loading) }
        coroutineScope.launch {
            when (val configuration = timetablePreferences.getConfiguration().firstOrNull()) {
                null -> roomsFlow.update { Resource(null, Status.NotFoundError) }
                else -> getRoomsFromApi(configuration.year, configuration.semesterId)
            }
        }
    }

    /**
     * Fetches the timetable for certain room from API again, to recover from a failed fetch. When
     * the room itself is missing, the rooms are fetched again first.
     */
    override fun refreshTimetable(roomId: String) {
        val timetableFlow = timetableFlows[roomId] ?: return
        timetableFlow.update { Resource(it.payload, Status.Loading) }

        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                timetableFlow.update { Resource(null, Status.NotFoundError) }
                return@launch
            }

            val room = roomsFlow.value.payload?.firstOrNull { it.id == roomId }
            if (room != null) {
                getEventsFromApi(configuration.year, configuration.semesterId, room)
                return@launch
            }

            // Once cached, the room is picked up by initializeEvents, so only failures are handled
            roomsFlow.update { Resource(it.payload, Status.Loading) }
            val resource = getRoomsFromApi(configuration.year, configuration.semesterId)
            if (resource.payload?.any { it.id == roomId } != true) {
                timetableFlow.update { Resource(null, resource.status.isNotFoundError()) }
            }
        }
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
     * Retrieves rooms from API and update the database of output flow, returning the API resource
     */
    private suspend fun getRoomsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Room>> {
        val resource = roomsDataSource.getRoomsFromApi(year, semesterId)
        val rooms = resource.payload

        when {
            resource.status.isSuccess() && rooms != null -> {
                roomsDataSource.saveRoomsInCache(rooms)
                roomsFlow.update {
                    when {
                        it.payload.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> roomsFlow.update {
                when {
                    it.payload.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }

        return resource
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
     * Initializes collection of database entries and possible API updates. Follows the rooms, so
     * the timetable is loaded once the room is available, or fails along with the rooms.
     */
    private fun initializeEvents(roomId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest

                roomsFlow
                    .map { resource ->
                        val room = resource.payload?.firstOrNull { it.id == roomId }
                        room to resource.status.takeIf { room == null }
                    }
                    .distinctUntilChanged()
                    .collectLatest { (room, status) ->
                        when {
                            room != null -> getEventsFromCache(
                                configuration.year,
                                configuration.semesterId,
                                room
                            )

                            status == null || status.isLoading() -> Unit
                            else -> timetableFlows[roomId]?.update {
                                Resource(null, status.isNotFoundError())
                            }
                        }
                    }
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
        val configurationId = year.toString() + semesterId
        val resource = roomsDataSource.getEventsFromApi(year, semesterId, room)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.updateEventsInCache(configurationId, room.id, events)
                timetableFlows[room.id]?.update {
                    when {
                        it.payload?.events.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> timetableFlows[room.id]?.update {
                when {
                    it.payload?.events.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }
    }

    private fun prefetchAll(
        year: Int,
        semesterId: String,
        rooms: List<Owner.Room>
    ) {
        coroutineScope.launch {
            rooms.map { room ->
                async { getEventsFromApi(year, semesterId, room) }
            }.awaitAll()
        }
    }
}