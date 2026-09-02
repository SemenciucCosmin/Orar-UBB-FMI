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

    suspend fun getNotificationsInitialSetup(): Boolean

    suspend fun setNotificationsInitialSetup(value: Boolean)

    companion object {
        const val PREFERENCES_NAME = "SETTINGS_PREFERENCES"
    }
}
