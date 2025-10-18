package com.ubb.fmi.orar.data.teachers.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    override fun getTeachers(): Flow<Resource<List<Owner.Teacher>>> {
        return teachersFlow.map { resource ->
            val sortedTeachers = resource.payload?.sortedWith(
                compareBy<Owner.Teacher> { it.title.orderIndex }.thenBy { it.name }
            )

            resource.copy(payload = sortedTeachers)
        }
    }

    override fun getTimetable(teacherId: String): Flow<Resource<Timetable<Owner.Teacher>>> {
        if (!timetableFlows.keys.contains(teacherId)) {
            timetableFlows[teacherId] = MutableStateFlow(Resource(null, Status.Loading))
            prefetchEvents(teacherId)
            initializeEvents(teacherId)
        }

        return timetableFlows[teacherId] ?: MutableStateFlow(Resource(null, Status.NotFoundError))
    }

    override suspend fun invalidate(year: Int, semesterId: String) {
        teachersDataSource.invalidate(year, semesterId)
    }

    private fun prefetchTeachers() {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let { getTeachersFromApi(it.year, it.semesterId) }
        }
    }

    private fun initializeTeachers() {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) {
                    teachersFlow.update { Resource(null, Status.NotFoundError) }
                    return@collectLatest
                }

                getTeachersFromCache(configuration.year, configuration.semesterId)
            }
        }
    }

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

    private suspend fun getTeachersFromApi(
        year: Int,
        semesterId: String,
    ) {
        val resource = teachersDataSource.getTeachersFromApi(year, semesterId)
        val teacher = resource.payload

        when {
            resource.status.isSuccess() && teacher != null -> {
                teachersDataSource.saveTeachersInCache(teacher)
            }

            else -> teachersFlow.update { Resource(null, resource.status) }
        }
    }

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

    private fun initializeEvents(teacherId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) {
                    timetableFlows[teacherId]?.update { Resource(null, Status.NotFoundError) }
                    return@collectLatest
                }

                val teacher = teachersDataSource.getTeachersFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { it.id == teacherId } ?: return@collectLatest

                getEventsFromCache(configuration.year, configuration.semesterId, teacher)
            }
        }
    }

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

    private suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        teacher: Owner.Teacher,
    ) {
        val resource = teachersDataSource.getEventsFromApi(year, semesterId, teacher)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.saveEventsInCache(teacher.id, events)
            }

            else -> timetableFlows[teacher.id]?.update { Resource(null, resource.status) }
        }
    }
}