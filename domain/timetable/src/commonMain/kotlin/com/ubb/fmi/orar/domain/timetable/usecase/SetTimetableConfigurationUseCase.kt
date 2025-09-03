package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.preferences.TimetablePreferences

class SetTimetableConfigurationUseCase(private val timetablePreferences: TimetablePreferences) {
    suspend operator fun invoke(
        year: Int,
        semesterId: String,
        userTypeId: String,
        fieldId: String?,
        studyLevelId: String?,
        studyLineDegreeId: String?,
        groupId: String?,
        teacherId: String?,
    ) {
        timetablePreferences.setYear(year)
        timetablePreferences.setSemester(semesterId)
        timetablePreferences.setUserType(userTypeId)

        fieldId?.let { timetablePreferences.setFieldId(it) }
        studyLevelId?.let { timetablePreferences.setStudyLevelId(it) }
        studyLineDegreeId?.let { timetablePreferences.setDegreeId(it) }
        groupId?.let { timetablePreferences.setGroupId(it) }
        teacherId?.let { timetablePreferences.setTeacherId(it) }
    }
}
