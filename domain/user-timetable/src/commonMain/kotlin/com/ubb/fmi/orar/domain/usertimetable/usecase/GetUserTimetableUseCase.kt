package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepository
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.usertimetable.model.UserType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.transformLatest

/**
 * Use case for retrieving the user's timetable based on their configuration.
 * This use case interacts with the groups and teachers data sources to fetch the timetable.
 * It checks the user's type (student or teacher) and retrieves the appropriate timetable data accordingly.
 */
@Suppress("UNCHECKED_CAST")
class GetUserTimetableUseCase(
    private val groupsRepository: GroupsRepository,
    private val teacherRepository: TeacherRepository,
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
        return timetablePreferences.getConfiguration().transformLatest { configuration ->
            logger.d(TAG, "configuration $configuration")

            if (configuration == null) {
                emit(Resource(null, Status.Error))
                return@transformLatest
            }

            when (UserType.getById(configuration.userTypeId)) {
                UserType.STUDENT -> {
                    val studyLevel = configuration.studyLevelId?.let { StudyLevel.getById(it) }
                    val fieldId = configuration.fieldId
                    val groupId = configuration.groupId

                    if (studyLevel == null || fieldId == null || groupId == null) {
                        emit(Resource(null, Status.Error))
                        return@transformLatest
                    }

                    groupsRepository.getTimetable(
                        studyLineId = fieldId + studyLevel.notation,
                        groupId = groupId,
                    ).collectLatest {
                        emit(it as Resource<Timetable<Owner>>)
                    }
                }

                UserType.TEACHER -> {
                    if (configuration.teacherId == null) {
                        emit(Resource(null, Status.Error))
                        return@transformLatest
                    }

                    teacherRepository.getTimetable(
                        teacherId = configuration.teacherId!!,
                    ).collectLatest {
                        emit(it as Resource<Timetable<Owner>>)
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "GetUserTimetableUseCase"
    }
}