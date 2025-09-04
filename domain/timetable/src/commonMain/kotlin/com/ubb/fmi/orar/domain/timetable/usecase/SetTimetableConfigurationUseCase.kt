package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences

/**
 * Use case for setting the timetable configuration based on user preferences.
 * This includes setting the academic year, semester, user type, and optional fields like field ID,
 * study level ID, study line degree ID, group ID, and teacher ID.
 *
 * @property timetablePreferences The preferences manager for storing timetable configurations.
 */
class SetTimetableConfigurationUseCase(private val timetablePreferences: TimetablePreferences) {

    /**
     * Sets the timetable configuration based on the provided parameters.
     *
     * @param year The academic year to set.
     * @param semesterId The ID of the semester to set.
     * @param userTypeId The ID of the user type to set.
     * @param fieldId Optional field ID to set.
     * @param studyLevelId Optional study level ID to set.
     * @param studyLineDegreeId Optional study line degree ID to set.
     * @param groupId Optional group ID to set.
     * @param teacherId Optional teacher ID to set.
     */
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
