package com.ubb.fmi.orar.domain.usertimetable.usecase

import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.studylines.usecase.GetStudyLineTimetableUseCase
import com.ubb.fmi.orar.domain.teachers.usecase.GetTeacherTimetableUseCase
import com.ubb.fmi.orar.domain.usertimetable.model.StudyYear
import com.ubb.fmi.orar.domain.usertimetable.model.Timetable
import com.ubb.fmi.orar.feature.timetable.ui.model.UserType
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import kotlinx.coroutines.flow.firstOrNull

class GetUserTimetableUseCase(
    private val getStudyLineTimetableUseCase: GetStudyLineTimetableUseCase,
    private val getTeacherTimetableUseCase: GetTeacherTimetableUseCase,
    private val timetablePreferences: TimetablePreferences
) {

    suspend operator fun invoke(): Resource<Timetable> {
        val configuration = timetablePreferences.getConfiguration().firstOrNull()
        if (configuration == null) return Resource(null, Status.Error)

        when (UserType.getById(configuration.userTypeId)) {
            UserType.STUDENT -> {
                val studyYear = configuration.studyLineYearId?.let { StudyYear.getById(it) }
                val studyLineBaseId = configuration.studyLineBaseId
                val groupId = configuration.groupId

                if (studyYear == null || studyLineBaseId == null || groupId == null) {
                    return Resource(null, Status.Error)
                }

                val studyLineId = studyLineBaseId + studyYear.notation
                return getStudyLineTimetableUseCase(
                    studyLineId = studyLineId,
                    studyGroupId = groupId
                )
            }

            UserType.TEACHER -> {
                if (configuration.teacherId == null) {
                    return Resource(null, Status.Error)
                }

                return getTeacherTimetableUseCase(
                    teacherId = configuration.teacherId
                )
            }
        }
    }
}