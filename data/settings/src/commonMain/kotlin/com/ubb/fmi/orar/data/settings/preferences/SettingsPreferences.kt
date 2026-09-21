package com.ubb.fmi.orar.data.settings.preferences

import kotlinx.coroutines.flow.Flow

/**
 * Interface for preferences with configured settings
 */
interface SettingsPreferences {

    /**
     * Get [Flow] with selected theme option
     */
    fun getThemeOption(): Flow<String>

    /**
     * Sets theme option
     */
    suspend fun setThemeOption(value: String)

    /**
     * Get [Flow] with whether event notifications are enabled overall, for reactive UI.
     */
    fun getNotificationsEnabledFlow(): Flow<Boolean>

    /**
     * Sets whether event notifications are enabled overall.
     */
    suspend fun setNotificationsEnabled(value: Boolean)

    /**
     * Snapshot of whether event notifications are currently enabled overall, for one-off reads
     * from business logic that doesn't need to react to further changes.
     */
    suspend fun isNotificationsEnabled(): Boolean

    /**
     * Get [Flow] with how many minutes in advance of an event's start its notification should
     * fire, for reactive UI.
     */
    fun getNotificationAdvanceMinutesFlow(): Flow<Int>

    /**
     * Sets how many minutes in advance of an event's start its notification should fire.
     */
    suspend fun setNotificationAdvanceMinutes(value: Int)

    /**
     * Snapshot of how many minutes in advance of an event's start its notification should fire,
     * for one-off reads from business logic that doesn't need to react to further changes.
     */
    suspend fun getNotificationAdvanceMinutes(): Int

    companion object {
        const val PREFERENCES_NAME = "SETTINGS_PREFERENCES"
    }
}
