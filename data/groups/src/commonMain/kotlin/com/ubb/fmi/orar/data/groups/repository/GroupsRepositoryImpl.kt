package com.ubb.fmi.orar.data.groups.repository

import com.ubb.fmi.orar.data.groups.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.network.model.isNotFoundError
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.studylines.repository.StudyLinesRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLine
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
 * Repository for managing data flow and data source for groups
 */
class GroupsRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val studyLinesRepository: StudyLinesRepository,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val groupsDataSource: GroupsDataSource,
    private val eventsDataSource: EventsDataSource,
    private val timetablePreferences: TimetablePreferences,
) : GroupsRepository {

    private val groupsFlow: MutableMap<String, MutableStateFlow<Resource<List<Owner.Group>>>> =
        mutableMapOf()

    private val timetableFlows: MutableMap<String, MutableStateFlow<Resource<Timetable<Owner.Group>>>> =
        mutableMapOf()

    /**
     * Retrieves a [Flow] of groups
     */
    override fun getGroups(studyLineId: String): Flow<Resource<List<Owner.Group>>> {
        if (!groupsFlow.keys.contains(studyLineId)) {
            groupsFlow[studyLineId] = MutableStateFlow(Resource(null, Status.Loading))
            prefetchGroups(studyLineId)
            initializeGroups(studyLineId)
        }

        return groupsFlow[studyLineId]?.map { resource ->
            val sortedGroups = resource.payload?.sortedBy { it.name }
            resource.copy(payload = sortedGroups)
        } ?: MutableStateFlow(Resource(null, Status.NotFoundError))
    }

    /**
     * Retrieves a [Flow] of timetable for certain group
     */
    override fun getTimetable(
        groupId: String,
        studyLineId: String,
    ): Flow<Resource<Timetable<Owner.Group>>> {
        if (!timetableFlows.keys.contains(groupId)) {
            timetableFlows[groupId] = MutableStateFlow(Resource(null, Status.Loading))
            val groupsFlow = getGroups(studyLineId)
            prefetchEvents(groupId, studyLineId)
            initializeEvents(groupId, groupsFlow)
        }

        return timetableFlows[groupId] ?: MutableStateFlow(Resource(null, Status.NotFoundError))
    }

    /**
     * Fetches the groups of certain study line from API again, to recover from a failed fetch.
     * When the study line itself is missing, the study lines are fetched again first.
     */
    override fun refreshGroups(studyLineId: String) {
        val groupsFlow = groupsFlow[studyLineId] ?: return
        groupsFlow.update { Resource(it.payload, Status.Loading) }

        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                groupsFlow.update { Resource(null, Status.NotFoundError) }
                return@launch
            }

            val studyLine = getStudyLine(studyLineId)
            if (studyLine != null) {
                getGroupsFromApi(configuration.year, configuration.semesterId, studyLine)
                return@launch
            }

            // Once cached, the study line is picked up by initializeGroups, so only failures are handled
            val status = getStudyLinesFromApi(configuration.year, configuration.semesterId, studyLineId)
            status?.let { groupsFlow.update { Resource(null, status) } }
        }
    }

    /**
     * Fetches the timetable for certain group from API again, to recover from a failed fetch. When
     * the group itself is missing, the groups, and the study lines if needed, are fetched again first.
     */
    override fun refreshTimetable(groupId: String, studyLineId: String) {
        val timetableFlow = timetableFlows[groupId] ?: return
        timetableFlow.update { Resource(it.payload, Status.Loading) }

        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            if (configuration == null) {
                timetableFlow.update { Resource(null, Status.NotFoundError) }
                return@launch
            }

            val year = configuration.year
            val semesterId = configuration.semesterId
            val group = groupsFlow[studyLineId]?.value?.payload?.firstOrNull { it.id == groupId }
            if (group != null) {
                getEventsFromApi(year, semesterId, group)
                return@launch
            }

            // Once cached, the group is picked up by initializeEvents, so only failures are handled
            groupsFlow[studyLineId]?.update { Resource(it.payload, Status.Loading) }
            val studyLine = getStudyLine(studyLineId)
            val status = when (studyLine) {
                null -> getStudyLinesFromApi(year, semesterId, studyLineId)?.also { status ->
                    groupsFlow[studyLineId]?.update { Resource(null, status) }
                }

                else -> {
                    val resource = getGroupsFromApi(year, semesterId, studyLine)
                    resource.status.isNotFoundError().takeIf {
                        resource.payload?.any { it.id == groupId } != true
                    }
                }
            }

            status?.let { timetableFlow.update { Resource(null, status) } }
        }
    }

    /**
     * Invalidates groups cache
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        groupsDataSource.invalidate(year, semesterId)
    }

    /**
     * Retrieves the study line with [studyLineId] from the loaded study lines
     */
    private suspend fun getStudyLine(studyLineId: String): StudyLine? {
        return studyLinesRepository.getStudyLines().firstOrNull()?.payload?.firstOrNull {
            it.id == studyLineId
        }
    }

    /**
     * Retrieves study lines from API and updates the database, returning the error status when
     * the study line with [studyLineId] couldn't be fetched, or null otherwise
     */
    private suspend fun getStudyLinesFromApi(
        year: Int,
        semesterId: String,
        studyLineId: String,
    ): Status? {
        val resource = studyLinesDataSource.getStudyLinesFromApi(year, semesterId)
        val studyLines = resource.payload

        if (resource.status.isSuccess() && studyLines != null) {
            studyLinesDataSource.saveStudyLinesInCache(studyLines)
        }

        return resource.status.isNotFoundError().takeIf {
            studyLines?.any { it.id == studyLineId } != true
        }
    }

    /**
     * Tries prefetching the groups from API for a safety update of local data
     */
    private fun prefetchGroups(studyLineId: String) {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let {
                val studyLine = studyLinesDataSource.getStudyLinesFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { studyLine ->
                    studyLine.id == studyLineId
                } ?: return@let

                getGroupsFromApi(it.year, it.semesterId, studyLine)
            }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates. Follows the study
     * lines, so the groups are loaded once the study line is available, or fail along with the
     * study lines.
     */
    private fun initializeGroups(studyLineId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest

                studyLinesRepository.getStudyLines()
                    .map { resource ->
                        val studyLine = resource.payload?.firstOrNull { it.id == studyLineId }
                        studyLine to resource.status.takeIf { studyLine == null }
                    }
                    .distinctUntilChanged()
                    .collectLatest { (studyLine, status) ->
                        when {
                            studyLine != null -> getGroupsFromCache(
                                configuration.year,
                                configuration.semesterId,
                                studyLine
                            )

                            status == null || status.isLoading() -> Unit
                            else -> groupsFlow[studyLineId]?.update {
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
    private suspend fun getGroupsFromCache(
        year: Int,
        semesterId: String,
        studyLine: StudyLine,
    ) {
        groupsDataSource.getGroupsFromCache(year, semesterId, studyLine).collectLatest { groups ->
            when {
                groups.isEmpty() -> getGroupsFromApi(year, semesterId, studyLine)
                else -> groupsFlow[studyLine.id]?.update { Resource(groups, Status.Success) }
            }
        }
    }

    /**
     * Retrieves groups from API and update the database of output flow, returning the API resource
     */
    private suspend fun getGroupsFromApi(
        year: Int,
        semesterId: String,
        studyLine: StudyLine,
    ): Resource<List<Owner.Group>> {
        val resource = groupsDataSource.getGroupsFromApi(year, semesterId, studyLine)
        val group = resource.payload

        when {
            resource.status.isSuccess() && group != null -> {
                groupsDataSource.saveGroupsInCache(group)
                groupsFlow[studyLine.id]?.update {
                    when {
                        it.payload.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> groupsFlow[studyLine.id]?.update {
                when {
                    it.payload.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }

        return resource
    }

    /**
     * Tries prefetching the group events from API for a safety update of local data
     */
    private fun prefetchEvents(groupId: String, studyLineId: String) {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()

            configuration?.let {
                val studyLine = studyLinesDataSource.getStudyLinesFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { studyLine ->
                    studyLine.id == studyLineId
                } ?: return@let

                val group = groupsDataSource.getGroupsFromCache(
                    configuration.year,
                    configuration.semesterId,
                    studyLine
                ).firstOrNull()?.firstOrNull { group ->
                    group.id == groupId
                } ?: return@let

                getEventsFromApi(it.year, it.semesterId, group)
            }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates. Follows the groups of
     * [groupsFlow], so the timetable is loaded once the group is available, or fails along with
     * the groups.
     */
    private fun initializeEvents(
        groupId: String,
        groupsFlow: Flow<Resource<List<Owner.Group>>>,
    ) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest

                groupsFlow
                    .map { resource ->
                        val group = resource.payload?.firstOrNull { it.id == groupId }
                        group to resource.status.takeIf { group == null }
                    }
                    .distinctUntilChanged()
                    .collectLatest { (group, status) ->
                        when {
                            group != null -> getEventsFromCache(
                                configuration.year,
                                configuration.semesterId,
                                group
                            )

                            status == null || status.isLoading() -> Unit
                            else -> timetableFlows[groupId]?.update {
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
        group: Owner.Group,
    ) {
        val configurationId = year.toString() + semesterId
        eventsDataSource.getEventsFromCache(configurationId, group.id).collectLatest { events ->

            when {
                events.isEmpty() -> getEventsFromApi(year, semesterId, group)
                else -> timetableFlows[group.id]?.update {
                    Resource(Timetable(group, events), Status.Success)
                }
            }
        }
    }

    /**
     * Retrieves group events from API and update the database of output flow
     */
    private suspend fun getEventsFromApi(
        year: Int,
        semesterId: String,
        group: Owner.Group,
    ) {
        val configurationId = year.toString() + semesterId
        val resource = groupsDataSource.getEventsFromApi(year, semesterId, group)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.updateEventsInCache(configurationId, group.id, events)
                timetableFlows[group.id]?.update {
                    when {
                        it.payload?.events.isNullOrEmpty() -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
            }

            else -> timetableFlows[group.id]?.update {
                when {
                    it.payload?.events.isNullOrEmpty() -> Resource(null, resource.status)
                    else -> it.copy(status = Status.Success)
                }
            }
        }
    }
}
