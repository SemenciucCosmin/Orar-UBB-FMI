package com.ubb.fmi.orar.domain.theme.usecase

import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

/**
 * Use case for getting the user chosen theme option on the application.
 */
class GetThemeOption(private val settingsPreferences: SettingsPreferences) {

    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(): Flow<ThemeOption> {
        return settingsPreferences.getThemeOption().mapLatest { themeOptionId ->
            ThemeOption.getById(themeOptionId)
        }
    }
}