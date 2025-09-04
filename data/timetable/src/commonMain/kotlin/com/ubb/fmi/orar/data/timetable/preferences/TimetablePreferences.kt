package com.ubb.fmi.orar.data.timetable.preferences

import com.ubb.fmi.orar.data.timetable.model.TimetableConfiguration
import kotlinx.coroutines.flow.Flow

/**
 * Interface for preferences with timetable user configuration
 */
interface TimetablePreferences {

    /**
     * Get [Flow] with [TimetableConfiguration] of user
     */
    suspend fun getConfiguration(): Flow<TimetableConfiguration?>

    /**
     * Sets [year]
     */
    suspend fun setYear(year: Int)

    /**
     * Sets [semesterId]
     */
    suspend fun setSemester(semesterId: String)

    /**
     * Sets [userTypeId]
     */
    suspend fun setUserType(userTypeId: String)

    /**
     * Sets [degreeId]
     */
    suspend fun setDegreeId(degreeId: String)

    /**
     * Sets [fieldId]
     */
    suspend fun setFieldId(fieldId: String)

    /**
     * Sets [studyLevelId]
     */
    suspend fun setStudyLevelId(studyLevelId: String)

    /**
     * Sets [groupId]
     */
    suspend fun setGroupId(groupId: String)

    /**
     * Sets [teacherId]
     */
    suspend fun setTeacherId(teacherId: String)
}
