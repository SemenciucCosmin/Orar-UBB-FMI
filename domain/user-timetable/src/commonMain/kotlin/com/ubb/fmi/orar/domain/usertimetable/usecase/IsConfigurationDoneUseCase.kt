package com.ubb.fmi.orar.domain.usertimetable.usecase

import Logger
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.usertimetable.model.UserType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

/**
 * Use case for checking if configuration is done or not
 */
class IsConfigurationDoneUseCase(
    private val timetablePreferences: TimetablePreferences,
    private val logger: Logger,
) {
    operator fun invoke(): Flow<Boolean> {
        return timetablePreferences.getConfiguration().map { configuration ->
            logger.d(TAG, "checkConfiguration configuration: $configuration")

            val hasDegree = configuration?.degreeId != null
            val hasStudyLineBase = configuration?.fieldId != null
            val hasStudyLineYear = configuration?.studyLevelId != null
            val hasStudyLine = hasStudyLineBase && hasStudyLineYear

            val hasGroup = configuration?.groupId != null
            val hasStudyLineInfo = hasStudyLine && hasGroup

            logger.d(TAG, "checkConfiguration hasGroup: $hasGroup")
            logger.d(TAG, "checkConfiguration hasStudyLineInfo: $hasStudyLineInfo")

            val isStudent = configuration?.userTypeId == UserType.STUDENT.id
            val isTeacher = configuration?.userTypeId == UserType.TEACHER.id

            val hasStudentInfo = isStudent && hasDegree && hasStudyLineInfo
            val hasTeacherInfo = isTeacher && configuration.teacherId != null

            logger.d(TAG, "checkConfiguration hasStudentInfo: $hasStudentInfo")
            logger.d(TAG, "checkConfiguration hasTeacherInfo: $hasTeacherInfo")

            val hasUserInfo = hasStudentInfo || hasTeacherInfo
            logger.d(TAG, "checkConfiguration hasUserInfo: $hasUserInfo")
            hasUserInfo
        }.distinctUntilChanged()
    }

    companion object {
        private const val TAG = "IsConfigurationDoneUseCase"
    }
}