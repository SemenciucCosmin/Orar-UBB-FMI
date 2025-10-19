package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.usertimetable.model.UserType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transformLatest

/**
 * Use case for retrieving the user's timetable based on their configuration.
 * This use case interacts with the groups and teachers data sources to fetch the timetable.
 * It checks the user's type (student or teacher) and retrieves the appropriate timetable data accordingly.
 */
class GetUserTimetableUseCase(
    private val groupsRepository: GroupsRepository,
    private val teacherRepository: TeacherRepository,
    private val eventsDataSource: EventsDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) {

    /**
     * Retrieves the user's timetable based on their configuration.
     * It checks the user's type and fetches the timetable data accordingly.
     * If the configuration is not set or invalid, it returns an error status.
     *
     * @return A flow of Resource containing the user's timetable or an error status.
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<Resource<List<Event>>> {
        return timetablePreferences.getConfiguration().transformLatest { configuration ->
            logger.d(TAG, "configuration $configuration")

            if (configuration == null) return@transformLatest
            val configurationId = configuration.year.toString() + configuration.semesterId

            val impersonalEventsFlow = when (UserType.getById(configuration.userTypeId)) {
                UserType.STUDENT -> {
                    val studyLevel = configuration.studyLevelId?.let { StudyLevel.getById(it) }
                    val fieldId = configuration.fieldId
                    val groupId = configuration.groupId
                    val studyLineId = studyLevel?.notation?.let { fieldId + it }

                    if (studyLineId == null || groupId == null) return@transformLatest
                    groupsRepository.getTimetable(groupId, studyLineId).map {
                        Resource(it.payload?.events, it.status)
                    }
                }

                UserType.TEACHER -> {
                    val teacherId = configuration.teacherId
                    if (teacherId == null) return@transformLatest

                    teacherRepository.getTimetable(teacherId).map {
                        Resource(it.payload?.events, it.status)
                    }
                }
            }

            val personalEventsFlow = eventsDataSource.getEventsFromCache(
                configurationId = configurationId,
                ownerId = Owner.User.id
            )

            combine(
                impersonalEventsFlow,
                personalEventsFlow
            ) { impersonalResource, personalEvents ->
                val impersonalEvents = impersonalResource.payload ?: emptyList()
                val events = impersonalEvents + personalEvents

                Resource(events, impersonalResource.status)
            }.collectLatest(::emit)
        }
    }

    companion object {
        private const val TAG = "GetUserTimetableUseCase"
    }
}