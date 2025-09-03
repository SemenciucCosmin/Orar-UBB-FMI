package com.ubb.fmi.orar.data.preferences

import kotlinx.coroutines.flow.Flow

interface TimetablePreferences {

    suspend fun getConfiguration(): Flow<TimetableConfiguration?>

    suspend fun setYear(year: Int)

    suspend fun setSemester(semesterId: String)

    suspend fun setUserType(userTypeId: String)

    suspend fun setDegreeId(degreeId: String)

    suspend fun setFieldId(fieldId: String)

    suspend fun setStudyLevelId(studyLevelId: String)

    suspend fun setGroupId(groupId: String)

    suspend fun setTeacherId(teacherId: String)
}
