package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.students.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.domain.usertimetable.model.UserType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Use case for retrieving the user's timetable based on their configuration.
 * This use case interacts with the groups and teachers data sources to fetch the timetable.
 * It checks the user's type (student or teacher) and retrieves the appropriate timetable data accordingly.
 * @property groupsDataSource The data source for groups operations.
 * @property teachersDataSource The data source for teachers operations.
 * @property timetablePreferences The preferences manager for storing and retrieving timetable configurations.
 */
@Suppress("UNCHECKED_CAST")
class GetUserTimetableUseCase(
    private val groupsDataSource: GroupsDataSource,
    private val teachersDataSource: TeachersDataSource,
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
    operator fun invoke(): Flow<Resource<Timetable<Owner>>> {
        return timetablePreferences.getConfiguration().mapLatest { configuration ->
            logger.d(TAG, "configuration $configuration")

            if (configuration == null) {
                return@mapLatest Resource(null, Status.Error)
            }

            when (UserType.getById(configuration.userTypeId)) {
                UserType.STUDENT -> {
                    val studyLevel = configuration.studyLevelId?.let { StudyLevel.getById(it) }
                    val fieldId = configuration.fieldId
                    val groupId = configuration.groupId

                    if (studyLevel == null || fieldId == null || groupId == null) {
                        return@mapLatest Resource(null, Status.Error)
                    }

                    return@mapLatest groupsDataSource.getTimetable(
                        year = configuration.year,
                        semesterId = configuration.semesterId,
                        studyLineId = fieldId + studyLevel.notation,
                        groupId = groupId,
                    ) as Resource<Timetable<Owner>>
                }

                UserType.TEACHER -> {
                    if (configuration.teacherId == null) {
                        return@mapLatest Resource(null, Status.Error)
                    }

                    return@mapLatest teachersDataSource.getTimetable(
                        year = configuration.year,
                        semesterId = configuration.semesterId,
                        teacherId = configuration.teacherId!!,
                    ) as Resource<Timetable<Owner>>
                }
            }
        }
    }

    companion object {
        private const val TAG = "GetUserTimetableUseCase"
    }
}