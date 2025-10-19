package com.ubb.fmi.orar.data.groups.repository

import com.ubb.fmi.orar.data.groups.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLine
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
 * Repository for managing data flow and data source for groups
 */
class GroupsRepositoryImpl(
    private val coroutineScope: CoroutineScope,
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
        if (groupsFlow[studyLineId]?.subscriptionCount?.value == 0) {
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
            prefetchEvents(groupId, studyLineId)
            initializeEvents(groupId, studyLineId)
        }

        return timetableFlows[groupId] ?: MutableStateFlow(Resource(null, Status.NotFoundError))
    }

    /**
     * Invalidates groups cache
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        groupsDataSource.invalidate(year, semesterId)
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
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeGroups(studyLineId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) {
                    groupsFlow[studyLineId]?.update { Resource(null, Status.NotFoundError) }
                    return@collectLatest
                }

                val studyLine = studyLinesDataSource.getStudyLinesFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { it.id == studyLineId } ?: return@collectLatest
                getGroupsFromCache(configuration.year, configuration.semesterId, studyLine)
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
     * Retrieves groups from API and update the database of output flow
     */
    private suspend fun getGroupsFromApi(
        year: Int,
        semesterId: String,
        studyLine: StudyLine,
    ) {
        val resource = groupsDataSource.getGroupsFromApi(year, semesterId, studyLine)
        val group = resource.payload

        when {
            resource.status.isSuccess() && group != null -> {
                groupsDataSource.saveGroupsInCache(group)
            }

            else -> groupsFlow[studyLine.id]?.update { Resource(null, resource.status) }
        }
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
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeEvents(groupId: String, studyLineId: String) {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) {
                    timetableFlows[groupId]?.update { Resource(null, Status.NotFoundError) }
                    return@collectLatest
                }

                val studyLine = studyLinesDataSource.getStudyLinesFromCache(
                    configuration.year,
                    configuration.semesterId
                ).firstOrNull()?.firstOrNull { it.id == studyLineId } ?: return@collectLatest

                val group = groupsDataSource.getGroupsFromCache(
                    configuration.year,
                    configuration.semesterId,
                    studyLine
                ).firstOrNull()?.firstOrNull { it.id == groupId } ?: return@collectLatest

                getEventsFromCache(configuration.year, configuration.semesterId, group)
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
        val resource = groupsDataSource.getEventsFromApi(year, semesterId, group)
        val events = resource.payload

        when {
            resource.status.isSuccess() && events != null -> {
                eventsDataSource.saveEventsInCache(group.id, events)
            }

            else -> timetableFlows[group.id]?.update { Resource(null, resource.status) }
        }
    }
}