package com.ubb.fmi.orar.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ubb.fmi.orar.data.preferences.model.TimetableConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

class TimetablePreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : TimetablePreferences {

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getConfiguration(): Flow<TimetableConfiguration?> {
        return dataStore.data.mapLatest { preferences ->
            val year = preferences[YEAR] ?: return@mapLatest null
            val semesterId = preferences[SEMESTER_ID] ?: return@mapLatest null
            val userTypeId = preferences[USER_TYPE_ID] ?: return@mapLatest null
            val degreeId = preferences[DEGREE_ID]
            val studyLineBaseId = preferences[STUDY_LINE_BASE_ID]
            val studyLineYearId = preferences[STUDY_LINE_YEAR_ID]
            val groupId = preferences[GROUP_ID]
            val groupTypeId = preferences[GROUP_TYPE_ID]
            val teacherId = preferences[TEACHER_ID]

            TimetableConfiguration(
                year = year,
                semesterId = semesterId,
                userTypeId = userTypeId,
                degreeId = degreeId,
                studyLineBaseId = studyLineBaseId,
                studyLineYearId = studyLineYearId,
                groupId = groupId,
                groupTypeId = groupTypeId,
                teacherId = teacherId,
            )
        }
    }

    override suspend fun setYear(year: Int) {
        dataStore.edit { it[YEAR] = year }
    }

    override suspend fun setSemester(semesterId: String) {
        dataStore.edit { it[SEMESTER_ID] = semesterId }
    }

    override suspend fun setUserType(userTypeId: String) {
        dataStore.edit { it[USER_TYPE_ID] = userTypeId }
    }

    override suspend fun setDegreeId(degreeId: String) {
        dataStore.edit { it[DEGREE_ID] = degreeId }
    }

    override suspend fun setStudyLineBaseId(studyLineBaseId: String) {
        dataStore.edit { it[STUDY_LINE_BASE_ID] = studyLineBaseId }
    }

    override suspend fun setStudyLineYearId(studyLineYearId: String) {
        dataStore.edit { it[STUDY_LINE_YEAR_ID] = studyLineYearId }
    }

    override suspend fun setGroupId(groupId: String) {
        dataStore.edit { it[GROUP_ID] = groupId }
    }

    override suspend fun setGroupTypeId(groupTypeId: String) {
        dataStore.edit { it[GROUP_TYPE_ID] = groupTypeId }
    }

    override suspend fun setTeacherId(teacherId: String) {
        dataStore.edit { it[TEACHER_ID] = teacherId }
    }

    companion object {
        val YEAR = intPreferencesKey(name = "YEAR")
        val SEMESTER_ID = stringPreferencesKey(name = "SEMESTER_ID")
        val USER_TYPE_ID = stringPreferencesKey(name = "USER_TYPE_ID")
        val DEGREE_ID = stringPreferencesKey(name = "DEGREE_ID")
        val STUDY_LINE_BASE_ID = stringPreferencesKey(name = "STUDY_LINE_BASE_ID")
        val STUDY_LINE_YEAR_ID = stringPreferencesKey(name = "STUDY_LINE_YEAR_ID")
        val GROUP_ID = stringPreferencesKey(name = "GROUP_ID")
        val GROUP_TYPE_ID = stringPreferencesKey(name = "GROUP_TYPE_ID")
        val TEACHER_ID = stringPreferencesKey(name = "TEACHER_ID")
    }
}
