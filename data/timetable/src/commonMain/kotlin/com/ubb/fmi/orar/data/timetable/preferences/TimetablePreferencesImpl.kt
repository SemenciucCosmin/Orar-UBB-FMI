package com.ubb.fmi.orar.data.timetable.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ubb.fmi.orar.data.timetable.model.TimetableConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Class for preferences with timetable user configuration
 */
class TimetablePreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : TimetablePreferences {

    /**
     * Get [Flow] with [TimetableConfiguration] of user
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getConfiguration(): Flow<TimetableConfiguration?> {
        return dataStore.data.mapLatest { preferences ->
            val year = preferences[YEAR] ?: return@mapLatest null
            val semesterId = preferences[SEMESTER_ID] ?: return@mapLatest null
            val userTypeId = preferences[USER_TYPE_ID] ?: return@mapLatest null
            val degreeId = preferences[DEGREE_ID]
            val fieldId = preferences[STUDY_LINE_BASE_ID]
            val studyLevelId = preferences[STUDY_LINE_YEAR_ID]
            val groupId = preferences[GROUP_ID]
            val teacherId = preferences[TEACHER_ID]

            TimetableConfiguration(
                year = year,
                semesterId = semesterId,
                userTypeId = userTypeId,
                degreeId = degreeId,
                fieldId = fieldId,
                studyLevelId = studyLevelId,
                groupId = groupId,
                teacherId = teacherId,
            )
        }
    }

    /**
     * Sets [year]
     */
    override suspend fun setYear(year: Int) {
        dataStore.edit { it[YEAR] = year }
    }

    /**
     * Sets [semesterId]
     */
    override suspend fun setSemester(semesterId: String) {
        dataStore.edit { it[SEMESTER_ID] = semesterId }
    }

    /**
     * Sets [userTypeId]
     */
    override suspend fun setUserType(userTypeId: String) {
        dataStore.edit { it[USER_TYPE_ID] = userTypeId }
    }

    /**
     * Sets [degreeId]
     */
    override suspend fun setDegreeId(degreeId: String) {
        dataStore.edit { it[DEGREE_ID] = degreeId }
    }

    /**
     * Sets [fieldId]
     */
    override suspend fun setFieldId(fieldId: String) {
        dataStore.edit { it[STUDY_LINE_BASE_ID] = fieldId }
    }

    /**
     * Sets [studyLevelId]
     */
    override suspend fun setStudyLevelId(studyLevelId: String) {
        dataStore.edit { it[STUDY_LINE_YEAR_ID] = studyLevelId }
    }

    /**
     * Sets [groupId]
     */
    override suspend fun setGroupId(groupId: String) {
        dataStore.edit { it[GROUP_ID] = groupId }
    }

    /**
     * Sets [teacherId]
     */
    override suspend fun setTeacherId(teacherId: String) {
        dataStore.edit { it[TEACHER_ID] = teacherId }
    }

    companion object {
        private val YEAR = intPreferencesKey(name = "YEAR")
        private val SEMESTER_ID = stringPreferencesKey(name = "SEMESTER_ID")
        private val USER_TYPE_ID = stringPreferencesKey(name = "USER_TYPE_ID")
        private val DEGREE_ID = stringPreferencesKey(name = "DEGREE_ID")
        private val STUDY_LINE_BASE_ID = stringPreferencesKey(name = "STUDY_LINE_BASE_ID")
        private val STUDY_LINE_YEAR_ID = stringPreferencesKey(name = "STUDY_LINE_YEAR_ID")
        private val GROUP_ID = stringPreferencesKey(name = "GROUP_ID")
        private val TEACHER_ID = stringPreferencesKey(name = "TEACHER_ID")
    }
}
