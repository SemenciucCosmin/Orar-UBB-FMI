package com.ubb.fmi.orar.domain.usertimetable.usecase

import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepository
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.model.UserType
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.flow.firstOrNull

/**
 * Use case for fetching the user's timetable from API again, to recover from a failed fetch.
 * The refreshed timetable is delivered through [GetUserTimetableUseCase].
 */
class RefreshUserTimetableUseCase(
    private val groupsRepository: GroupsRepository,
    private val teacherRepository: TeacherRepository,
    private val timetablePreferences: TimetablePreferences,
) {

    suspend operator fun invoke() {
        val configuration = timetablePreferences.getConfiguration().firstOrNull() ?: return

        when (UserType.getById(configuration.userTypeId)) {
            UserType.STUDENT -> {
                val studyLevel = configuration.studyLevelId?.let { StudyLevel.getById(it) }
                val studyLineId = studyLevel?.notation?.let { configuration.fieldId + it }
                val groupId = configuration.groupId

                if (studyLineId == null || groupId == null) return
                groupsRepository.refreshTimetable(groupId, studyLineId)
            }

            UserType.TEACHER -> {
                val teacherId = configuration.teacherId ?: return
                teacherRepository.refreshTimetable(teacherId)
            }
        }
    }
}
