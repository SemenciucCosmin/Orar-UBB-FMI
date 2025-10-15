package com.ubb.fmi.orar.data.preferences.migration

import androidx.datastore.core.DataMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey

/**
 * Class for migrating old preferences keys
 */
class PreferencesMigration : DataMigration<Preferences> {

    override suspend fun cleanUp() {
        /// Nothing to clean
    }

    override suspend fun migrate(currentData: Preferences): Preferences {
        val mutablePreferences = currentData.toMutablePreferences()

        currentData[STUDY_LINE_BASE_ID]?.let { oldValue ->
            mutablePreferences[FIELD_ID] = oldValue
            mutablePreferences.remove(STUDY_LINE_BASE_ID)
        }

        currentData[STUDY_LINE_YEAR_ID]?.let { oldValue ->
            mutablePreferences[STUDY_LEVEL_ID] = oldValue
            mutablePreferences.remove(STUDY_LINE_YEAR_ID)
        }

        return mutablePreferences.toPreferences()
    }

    override suspend fun shouldMigrate(currentData: Preferences): Boolean {
        // Check if old keys exist
        return listOf(
            STUDY_LINE_BASE_ID,
            STUDY_LINE_YEAR_ID
        ).any { currentData.contains(it) }
    }

    companion object {
        // OLD KEYS
        private val STUDY_LINE_BASE_ID = stringPreferencesKey(name = "STUDY_LINE_BASE_ID")
        private val STUDY_LINE_YEAR_ID = stringPreferencesKey(name = "STUDY_LINE_YEAR_ID")

        // NEW KEYS
        private val FIELD_ID = stringPreferencesKey(name = "FIELD_ID")
        private val STUDY_LEVEL_ID = stringPreferencesKey(name = "STUDY_LEVEL_ID")
    }
}