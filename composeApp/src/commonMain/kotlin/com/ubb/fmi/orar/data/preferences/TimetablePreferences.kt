package com.ubb.fmi.orar.data.preferences

import com.ubb.fmi.orar.data.preferences.model.TimetableConfiguration
import kotlinx.coroutines.flow.Flow

interface TimetablePreferences {

    suspend fun getConfiguration(): Flow<TimetableConfiguration?>

    suspend fun setYear(year: Int)

    suspend fun setSemester(semesterId: String)

    suspend fun setUserType(userTypeId: String)

    suspend fun setDegreeId(degreeId: String)

    suspend fun setSubjectId(subjectId: String)

    suspend fun setTeacherId(teacherId: String)
}
