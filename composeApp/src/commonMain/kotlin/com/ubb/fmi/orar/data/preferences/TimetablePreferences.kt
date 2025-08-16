package com.ubb.fmi.orar.data.preferences

import com.ubb.fmi.orar.data.preferences.model.TimetableConfiguration
import kotlinx.coroutines.flow.Flow

interface TimetablePreferences {

    suspend fun getConfiguration(): Flow<TimetableConfiguration?>

    suspend fun setYear(year: Int)

    suspend fun setSemester(semesterId: String)

    suspend fun setUserType(userTypeId: String)

    suspend fun setDegreeId(degreeId: String)

    suspend fun setStudyLineBaseId(studyLineBaseId: String)

    suspend fun setStudyLineYearId(studyLineYearId: String)

    suspend fun setGroupId(groupId: String)

    suspend fun setGroupTypeId(groupTypeId: String)

    suspend fun setTeacherId(teacherId: String)
}
