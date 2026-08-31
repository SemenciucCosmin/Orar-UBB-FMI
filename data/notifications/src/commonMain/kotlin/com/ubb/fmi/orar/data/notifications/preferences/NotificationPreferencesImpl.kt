package com.ubb.fmi.orar.data.notifications.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

/**
 * Preferences for notifications feature
 */
class NotificationPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : NotificationPreferences {

    /**
     * Gets scheduled notification IDs as a [Flow]
     */
    override fun getScheduledIds(): Flow<Set<String>> {
        return dataStore.data.map { preferences ->
            preferences[SCHEDULED_IDS]?.split(DELIMITER)?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
        }.distinctUntilChanged()
    }

    /**
     * Adds a notification ID to the scheduled set
     */
    override suspend fun addScheduledId(id: String) {
        dataStore.edit { preferences ->
            val current = preferences[SCHEDULED_IDS]?.split(DELIMITER)?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf()
            current.add(id)
            preferences[SCHEDULED_IDS] = current.joinToString(DELIMITER)
        }
    }

    /**
     * Removes a notification ID from the scheduled set
     */
    override suspend fun removeScheduledId(id: String) {
        dataStore.edit { preferences ->
            val current = preferences[SCHEDULED_IDS]?.split(DELIMITER)?.filter { it.isNotBlank() }?.toMutableSet() ?: mutableSetOf()
            current.remove(id)
            if (current.isEmpty()) {
                preferences.remove(SCHEDULED_IDS)
            } else {
                preferences[SCHEDULED_IDS] = current.joinToString(DELIMITER)
            }
        }
    }

    /**
     * Clears all scheduled notification IDs
     */
    override suspend fun clearScheduledIds() {
        dataStore.edit { it.remove(SCHEDULED_IDS) }
    }

    companion object {
        private const val DELIMITER = ","
        private val SCHEDULED_IDS = stringPreferencesKey("SCHEDULED_IDS")
    }
}
