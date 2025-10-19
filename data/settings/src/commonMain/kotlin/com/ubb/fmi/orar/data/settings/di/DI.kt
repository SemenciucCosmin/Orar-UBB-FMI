package com.ubb.fmi.orar.data.settings.di

import com.ubb.fmi.orar.data.preferences.factory.DataStoreFactory
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferencesImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for settings data layer.
 */
fun settingsDataModule() = module {
    single<SettingsPreferences> {
        SettingsPreferencesImpl(
            get<DataStoreFactory>().create(SettingsPreferences.PREFERENCES_NAME)
        )
    }
}
