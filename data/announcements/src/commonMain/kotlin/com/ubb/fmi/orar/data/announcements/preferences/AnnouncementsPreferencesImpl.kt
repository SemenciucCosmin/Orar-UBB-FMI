package com.ubb.fmi.orar.data.announcements.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
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
     * Gets whether the current update announcement was shown
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getUpdateAnnouncementShown(): Flow<Boolean> {
        return dataStore.data.mapLatest {
            (it[LAST_SHOWN_UPDATE_ANNOUNCEMENT_VERSION] ?: 0) >= UPDATE_ANNOUNCEMENT_VERSION
        }
    }

    /**
     * Marks the current update announcement as shown
     */
    override suspend fun setUpdateAnnouncementShown() {
        dataStore.edit {
            it[LAST_SHOWN_UPDATE_ANNOUNCEMENT_VERSION] = UPDATE_ANNOUNCEMENT_VERSION
        }
    }

    companion object {
        /**
         * Increment for every new update announcement, so that it is shown once again to all users
         */
        private const val UPDATE_ANNOUNCEMENT_VERSION = 1
        private val LAST_SHOWN_UPDATE_ANNOUNCEMENT_VERSION = intPreferencesKey(
            "LAST_SHOWN_UPDATE_ANNOUNCEMENT"
        )
    }
}
