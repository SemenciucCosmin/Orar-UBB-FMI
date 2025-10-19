package com.ubb.fmi.orar.data.subjects.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
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
 * Repository for managing data flow and data source for subjects
 */
class SubjectsRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val subjectsDataSource: SubjectsDataSource,
    private val eventsDataSource: EventsDataSource,
    private val timetablePreferences: TimetablePreferences,
) : SubjectsRepository {

    private val subjectsFlow: MutableStateFlow<Resource<List<Owner.Subject>>> = MutableStateFlow(
        Resource(null, Status.Loading)
    )

    private val timetableFlows: MutableMap<String, MutableStateFlow<Resource<Timetable<Owner.Subject>>>> =
        mutableMapOf()

    init {
        prefetchSubjects()
        initializeSubjects()
    }

    /**
     * Retrieves a [Flow] of subjects
     */
    override fun getSubjects(): Flow<Resource<List<Owner.Subject>>> {
        return subjectsFlow.map { resource ->
            val sortedSubjects = resource.payload?.sortedBy { it.name }
            resource.copy(payload = sortedSubjects)
        }
    }

    /**
     * Retrieves a [Flow] of timetable for certain subjects
     */
    override fun getTimetable(subjectId: String): Flow<Resource<Timetable<Owner.Subject>>> {
        if (!timetableFlows.keys.contains(subjectId)) {
            timetableFlows[subjectId] = MutableStateFlow(Resource(null, Status.Loading))
            prefetchEvents(subjectId)
            initializeEvents(subjectId)
        }

        return timetableFlows[subjectId] ?: MutableStateFlow(Resource(null, Status.NotFoundError))
    }

    /**
     * Invalidates subjects cache
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        subjectsDataSource.invalidate(year, semesterId)
    }

    /**
     * Tries prefetching the subject events from API for a safety update of local data
     */
    private fun prefetchSubjects() {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let { getSubjectsFromApi(it.year, it.semesterId) }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeSubjects() {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest
                getSubjectsFromCache(configuration.year, configuration.semesterId)
            }
        }
    }

    /**
     * Provides the collection of database data flow
     */
    private suspend fun getSubjectsFromCache(
        year: Int,
        semesterId: String,
    ) {
        subjectsDataSource.getSubjectsFromCache(year, semesterId).collectLatest { subjects ->
            when {
                subjects.isEmpty() -> getSubjectsFromApi(year, semesterId)
                else -> subjectsFlow.update { Resource(subjects, Status.Success) }
            }
        }
    }

    /**
     * Retrieves subjects from API and update the database of output flow
     */
    private suspend fun getSubjectsFromApi(
        year: Int,
        semesterId: String,
    ) {
        val resource = subjectsDataSource.getSubjectsFromApi(year, semesterId)
        val subject = resource.payload

        when {
            resource.status.isSuccess() && subject != null -> {
                subjectsDataSource.saveSubjectsInCache(subject)
            }

            else -> subjectsFlow.update {
                when {
                    it.payload.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it
                }
            }
        }
    }

    /**
     * Tries prefetching the subject events from API for a safety update of local data
     */
    private fun prefetchEvents(subjectId: String) {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let {
                val subject = subjectsDataSource.getSubjectsFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { subject ->
                    subject.id == subjectId
                } ?: return@let

                getEventsFromApi(it.year, it.semesterId, subject)
            }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeEvents(subjectId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest

                val subject = subjectsDataSource.getSubjectsFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { it.id == subjectId } ?: return@collectLatest

                getEventsFromCache(configuration.year, configuration.semesterId, subject)
            }
        }
    }

    /**
     * Provides the collection of database data flow
     */
    private suspend fun getEventsFromCache(
        year: Int,
        semesterId: String,
        subject: Owner.Subject,
    ) {
        val configurationId = year.toString() + semesterId
        eventsDataSource.getEventsFromCache(configurationId, subject.id).collectLatest { events ->
            when {
                events.isEmpty() -> getEventsFromApi(year, semesterId, subject)
                else -> timetableFlows[subject.id]?.update {
                    Resource(Timetable(subject, events), Status.Success)
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
        subject: Owner.Subject,
    ) {
        val resource = subjectsDataSource.getEventsFromApi(year, semesterId, subject)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.saveEventsInCache(subject.id, events)
            }

            else -> timetableFlows[subject.id]?.update {
                when {
                    it.payload?.events.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it
                }
            }
        }
    }
}