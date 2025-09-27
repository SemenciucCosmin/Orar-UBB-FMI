package com.ubb.fmi.orar.data.timetable.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.ubb.fmi.orar.data.timetable.model.TimetableConfiguration
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
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
            val fieldId = preferences[FIELD_ID]
            val studyLevelId = preferences[STUDY_LEVEL_ID]
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
        dataStore.edit { it[FIELD_ID] = fieldId }
    }

    /**
     * Sets [studyLevelId]
     */
    override suspend fun setStudyLevelId(studyLevelId: String) {
        dataStore.edit { it[STUDY_LEVEL_ID] = studyLevelId }
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

    /**
     * Clear data and then sets it again to trigger recollection of data
     */
    override suspend fun refresh() {
        val configuration = getConfiguration().firstOrNull()
        configuration?.let {
            dataStore.edit { it.clear() }
            dataStore.edit { preferences ->
                preferences[YEAR] = configuration.year
                preferences[SEMESTER_ID] = configuration.semesterId
                preferences[USER_TYPE_ID] = configuration.userTypeId
                configuration.degreeId?.let { preferences[DEGREE_ID] = it }
                configuration.fieldId?.let { preferences[FIELD_ID] = it }
                configuration.studyLevelId?.let { preferences[STUDY_LEVEL_ID] = it }
                configuration.groupId?.let { preferences[GROUP_ID] = it }
                configuration.teacherId?.let { preferences[TEACHER_ID] = it }
            }
        }
    }

    companion object {
        private val YEAR = intPreferencesKey(name = "YEAR")
        private val SEMESTER_ID = stringPreferencesKey(name = "SEMESTER_ID")
        private val USER_TYPE_ID = stringPreferencesKey(name = "USER_TYPE_ID")
        private val DEGREE_ID = stringPreferencesKey(name = "DEGREE_ID")
        private val FIELD_ID = stringPreferencesKey(name = "FIELD_ID")
        private val STUDY_LEVEL_ID = stringPreferencesKey(name = "STUDY_LEVEL_ID")
        private val GROUP_ID = stringPreferencesKey(name = "GROUP_ID")
        private val TEACHER_ID = stringPreferencesKey(name = "TEACHER_ID")
    }
}
