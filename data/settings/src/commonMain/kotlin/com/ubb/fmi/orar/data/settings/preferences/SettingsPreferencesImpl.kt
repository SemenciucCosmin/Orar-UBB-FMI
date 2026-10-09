package com.ubb.fmi.orar.data.settings.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

/**
 * Class for preferences with configured settings
 */
class SettingsPreferencesImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsPreferences {

    /**
     * Get [Flow] with selected theme option
     */
    override fun getThemeOption(): Flow<String> {
        return dataStore.data.map {
            it[THEME_OPTION] ?: DEFAULT_THEME_OPTION
        }.distinctUntilChanged()
    }

    /**
     * Sets theme option
     */
    override suspend fun setThemeOption(value: String) {
        dataStore.edit { it[THEME_OPTION] = value }
    }

    override fun getNotificationsEnabledFlow(): Flow<Boolean> {
        return dataStore.data.map {
            it[NOTIFICATIONS_ENABLED] ?: DEFAULT_NOTIFICATIONS_ENABLED
        }.distinctUntilChanged()
    }

    override suspend fun setNotificationsEnabled(value: Boolean) {
        dataStore.edit { it[NOTIFICATIONS_ENABLED] = value }
    }

    override suspend fun isNotificationsEnabled(): Boolean {
        return dataStore.data.map {
            it[NOTIFICATIONS_ENABLED] ?: DEFAULT_NOTIFICATIONS_ENABLED
        }.distinctUntilChanged().firstOrNull() ?: DEFAULT_NOTIFICATIONS_ENABLED
    }

    override fun getNotificationAdvanceMinutesFlow(): Flow<Int> {
        return dataStore.data.map {
            it[NOTIFICATION_ADVANCE_MINUTES] ?: DEFAULT_NOTIFICATION_ADVANCE_MINUTES
        }.distinctUntilChanged()
    }

    override suspend fun setNotificationAdvanceMinutes(value: Int) {
        dataStore.edit { it[NOTIFICATION_ADVANCE_MINUTES] = value }
    }

    override suspend fun getNotificationAdvanceMinutes(): Int {
        return dataStore.data.map {
            it[NOTIFICATION_ADVANCE_MINUTES]
        }.distinctUntilChanged().firstOrNull() ?: DEFAULT_NOTIFICATION_ADVANCE_MINUTES
    }

    companion object {
        private const val DEFAULT_THEME_OPTION = "system"
        private const val DEFAULT_NOTIFICATIONS_ENABLED = true
        private const val DEFAULT_NOTIFICATION_ADVANCE_MINUTES = 30
        private val THEME_OPTION = stringPreferencesKey("THEME_OPTION")
        private val NOTIFICATIONS_ENABLED = booleanPreferencesKey("NOTIFICATIONS_ENABLED")
        private val NOTIFICATION_ADVANCE_MINUTES = intPreferencesKey("NOTIFICATION_ADVANCE_MINUTES")
    }
}
