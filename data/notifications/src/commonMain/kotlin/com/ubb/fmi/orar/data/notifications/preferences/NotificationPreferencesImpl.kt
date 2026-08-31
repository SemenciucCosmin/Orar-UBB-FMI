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

    /**
     * Returns the serialized data for the given notification ID, or null if not found
     */
    override suspend fun getNotificationData(id: String): String? {
        return dataStore.data.map { it[notificationDataKey(id)] }.first()
    }

    /**
     * Stores serialized data for the given notification ID
     */
    override suspend fun setNotificationData(id: String, data: String) {
        dataStore.edit { it[notificationDataKey(id)] = data }
    }

    /**
     * Removes stored data for the given notification ID
     */
    override suspend fun removeNotificationData(id: String) {
        dataStore.edit { it.remove(notificationDataKey(id)) }
    }

    private fun notificationDataKey(id: String) = stringPreferencesKey("${DATA_KEY_PREFIX}$id")

    companion object {
        private const val DELIMITER = ","
        private const val DATA_KEY_PREFIX = "DATA_"
        private val SCHEDULED_IDS = stringPreferencesKey("SCHEDULED_IDS")
    }
}
