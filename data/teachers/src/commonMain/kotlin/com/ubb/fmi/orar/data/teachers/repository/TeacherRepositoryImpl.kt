package com.ubb.fmi.orar.data.teachers.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.network.model.isNotFoundError
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Repository for managing data flow and data source for teachers
 */
class TeacherRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val teachersDataSource: TeachersDataSource,
    private val eventsDataSource: EventsDataSource,
    private val timetablePreferences: TimetablePreferences,
) : TeacherRepository {

    private val teachersFlow: MutableStateFlow<Resource<List<Owner.Teacher>>> = MutableStateFlow(
        Resource(null, Status.Loading)
    )

    private val timetableFlows: MutableMap<String, MutableStateFlow<Resource<Timetable<Owner.Teacher>>>> =
        mutableMapOf()

    init {
        prefetchTeachers()
        initializeTeachers()
    }

    /**
     * Retrieves a [Flow] of teachers
     */
    override fun getTeachers(): Flow<Resource<List<Owner.Teacher>>> {
        return teachersFlow.map { resource ->
            val sortedTeachers = resource.payload?.sortedWith(
                compareBy<Owner.Teacher> { it.title.orderIndex }.thenBy { it.name }
            )

            resource.copy(payload = sortedTeachers)
        }
    }

    /**
     * Retrieves a [Flow] of timetable for certain teacher
     */
    override fun getTimetable(teacherId: String): Flow<Resource<Timetable<Owner.Teacher>>> {
        if (!timetableFlows.keys.contains(teacherId)) {
            timetableFlows[teacherId] = MutableStateFlow(Resource(null, Status.Loading))
            prefetchEvents(teacherId)
            initializeEvents(teacherId)
        }

        return timetableFlows[teacherId] ?: MutableStateFlow(Resource(null, Status.NotFoundError))
    }

    /**
     * Invalidates teachers cache
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        teachersDataSource.invalidate(year, semesterId)
    }

    /**
     * Fetches the teachers from API again, to recover from a failed fetch
     */
    override fun refresh() {
        teachersFlow.update { Resource(it.payload, Status.Loading) }
        coroutineScope.launch {
            when (val configuration = timetablePreferences.getConfiguration().firstOrNull()) {
                null -> teachersFlow.update { Resource(null, Status.NotFoundError) }
                else -> getTeachersFromApi(configuration.year, configuration.semesterId)
            }
        }
    }

    /**
     * Fetches the timetable for certain teacher from API again, to recover from a failed fetch. When
     * the teacher itself is missing, the teachers are fetched again first.
     */
    override fun refreshTimetable(teacherId: String) {
        val timetableFlow = timetableFlows[teacherId] ?: return
        timetableFlow.update { Resource(it.payload, Status.Loading) }

        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                timetableFlow.update { Resource(null, Status.NotFoundError) }
                return@launch
            }

            val teacher = teachersFlow.value.payload?.firstOrNull { it.id == teacherId }
            if (teacher != null) {
                getEventsFromApi(configuration.year, configuration.semesterId, teacher)
                return@launch
            }

            // Once cached, the teacher is picked up by initializeEvents, so only failures are handled
            teachersFlow.update { Resource(it.payload, Status.Loading) }
            val resource = getTeachersFromApi(configuration.year, configuration.semesterId)
            if (resource.payload?.any { it.id == teacherId } != true) {
                timetableFlow.update { Resource(null, resource.status.isNotFoundError()) }
            }
        }
    }

    /**
     * Tries prefetching the teacher events from API for a safety update of local data
     */
    private fun prefetchTeachers() {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let { getTeachersFromApi(it.year, it.semesterId) }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeTeachers() {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest
                getTeachersFromCache(configuration.year, configuration.semesterId)
            }
        }
    }

    /**
     * Provides the collection of database data flow
     */
    private suspend fun getTeachersFromCache(
        year: Int,
        semesterId: String,
    ) {
        teachersDataSource.getTeachersFromCache(year, semesterId).collectLatest { teachers ->
            when {
                teachers.isEmpty() -> getTeachersFromApi(year, semesterId)
                else -> teachersFlow.update { Resource(teachers, Status.Success) }
            }
        }
    }

    /**
     * Retrieves teachers from API and update the database of output flow, returning the API resource
     */
    private suspend fun getTeachersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Teacher>> {
        val resource = teachersDataSource.getTeachersFromApi(year, semesterId)
        val teacher = resource.payload

        when {
            resource.status.isSuccess() && teacher != null -> {
                teachersDataSource.saveTeachersInCache(teacher)
                teachersFlow.update {
                    when {
                        it.payload.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> teachersFlow.update {
                when {
                    it.payload.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }

        return resource
    }

    /**
     * Tries prefetching the teacher events from API for a safety update of local data
     */
    private fun prefetchEvents(teacherId: String) {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let {
                val teacher = teachersDataSource.getTeachersFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { it.id == teacherId } ?: return@let

                getEventsFromApi(it.year, it.semesterId, teacher)
            }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates. Follows the teachers, so
     * the timetable is loaded once the teacher is available, or fails along with the teachers.
     */
    private fun initializeEvents(teacherId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest

                teachersFlow
                    .map { resource ->
                        val teacher = resource.payload?.firstOrNull { it.id == teacherId }
                        teacher to resource.status.takeIf { teacher == null }
                    }
                    .distinctUntilChanged()
                    .collectLatest { (teacher, status) ->
                        when {
                            teacher != null -> getEventsFromCache(
                                configuration.year,
                                configuration.semesterId,
                                teacher
                            )

                            status == null || status.isLoading() -> Unit
                            else -> timetableFlows[teacherId]?.update {
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
        teacher: Owner.Teacher,
    ) {
        val configurationId = year.toString() + semesterId
        eventsDataSource.getEventsFromCache(configurationId, teacher.id).collectLatest { events ->
            when {
                events.isEmpty() -> getEventsFromApi(year, semesterId, teacher)
                else -> timetableFlows[teacher.id]?.update {
                    Resource(Timetable(teacher, events), Status.Success)
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
        teacher: Owner.Teacher,
    ) {
        val configurationId = year.toString() + semesterId
        val resource = teachersDataSource.getEventsFromApi(year, semesterId, teacher)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.updateEventsInCache(configurationId, teacher.id, events)
                timetableFlows[teacher.id]?.update {
                    when {
                        it.payload?.events.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> timetableFlows[teacher.id]?.update {
                when {
                    it.payload?.events.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }
    }
}