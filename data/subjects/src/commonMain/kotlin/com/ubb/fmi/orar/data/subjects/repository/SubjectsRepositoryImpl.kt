package com.ubb.fmi.orar.data.subjects.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.network.model.isNotFoundError
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
import kotlinx.coroutines.flow.distinctUntilChanged
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
     * Fetches the subjects from API again, to recover from a failed fetch
     */
    override fun refresh() {
        subjectsFlow.update { Resource(it.payload, Status.Loading) }
        coroutineScope.launch {
            when (val configuration = timetablePreferences.getConfiguration().firstOrNull()) {
                null -> subjectsFlow.update { Resource(null, Status.NotFoundError) }
                else -> getSubjectsFromApi(configuration.year, configuration.semesterId)
            }
        }
    }

    /**
     * Fetches the timetable for certain subject from API again, to recover from a failed fetch. When
     * the subject itself is missing, the subjects are fetched again first.
     */
    override fun refreshTimetable(subjectId: String) {
        val timetableFlow = timetableFlows[subjectId] ?: return
        timetableFlow.update { Resource(it.payload, Status.Loading) }

        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                timetableFlow.update { Resource(null, Status.NotFoundError) }
                return@launch
            }

            val subject = subjectsFlow.value.payload?.firstOrNull { it.id == subjectId }
            if (subject != null) {
                getEventsFromApi(configuration.year, configuration.semesterId, subject)
                return@launch
            }

            // Once cached, the subject is picked up by initializeEvents, so only failures are handled
            subjectsFlow.update { Resource(it.payload, Status.Loading) }
            val resource = getSubjectsFromApi(configuration.year, configuration.semesterId)
            if (resource.payload?.any { it.id == subjectId } != true) {
                timetableFlow.update { Resource(null, resource.status.isNotFoundError()) }
            }
        }
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
     * Retrieves subjects from API and update the database of output flow, returning the API resource
     */
    private suspend fun getSubjectsFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner.Subject>> {
        val resource = subjectsDataSource.getSubjectsFromApi(year, semesterId)
        val subject = resource.payload

        when {
            resource.status.isSuccess() && subject != null -> {
                subjectsDataSource.saveSubjectsInCache(subject)
                subjectsFlow.update {
                    when {
                        it.payload.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> subjectsFlow.update {
                when {
                    it.payload.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }

        return resource
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
     * Initializes collection of database entries and possible API updates. Follows the subjects, so
     * the timetable is loaded once the subject is available, or fails along with the subjects.
     */
    private fun initializeEvents(subjectId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest

                subjectsFlow
                    .map { resource ->
                        val subject = resource.payload?.firstOrNull { it.id == subjectId }
                        subject to resource.status.takeIf { subject == null }
                    }
                    .distinctUntilChanged()
                    .collectLatest { (subject, status) ->
                        when {
                            subject != null -> getEventsFromCache(
                                configuration.year,
                                configuration.semesterId,
                                subject
                            )

                            status == null || status.isLoading() -> Unit
                            else -> timetableFlows[subjectId]?.update {
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
        val configurationId = year.toString() + semesterId
        val resource = subjectsDataSource.getEventsFromApi(year, semesterId, subject)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.updateEventsInCache(configurationId, subject.id, events)
                timetableFlows[subject.id]?.update {
                    when {
                        it.payload?.events.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> timetableFlows[subject.id]?.update {
                when {
                    it.payload?.events.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }
    }
}