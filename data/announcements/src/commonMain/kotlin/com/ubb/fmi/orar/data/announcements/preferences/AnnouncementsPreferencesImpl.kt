package com.ubb.fmi.orar.data.announcements.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Preferences for announcements feature
 */
class AnnouncementsPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : AnnouncementsPreferences {

    /**
     * Gets whether the update announcement was shown
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUpdateAnnouncementShown(): Flow<Boolean> {
        return dataStore.data.mapLatest { it[UPDATE_ANNOUNCEMENT_SHOWN] ?: false }
    }

    /**
     * Marks update announcement as shown
     */
    override suspend fun setUpdateAnnouncementShown() {
        dataStore.edit { it[UPDATE_ANNOUNCEMENT_SHOWN] = true }
    }

    companion object {
        private val UPDATE_ANNOUNCEMENT_SHOWN = booleanPreferencesKey("UPDATE_ANNOUNCEMENT_SHOWN")
    }
}
