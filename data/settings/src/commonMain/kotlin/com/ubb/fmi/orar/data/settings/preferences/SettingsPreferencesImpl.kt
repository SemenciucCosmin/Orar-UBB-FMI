package com.ubb.fmi.orar.data.settings.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
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

    companion object {
        private val THEME_OPTION = stringPreferencesKey(name = "THEME_OPTION")
        private const val DEFAULT_THEME_OPTION = "system"
    }
}
