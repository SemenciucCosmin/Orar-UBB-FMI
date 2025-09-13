package com.ubb.fmi.orar.feature.settings.di

import com.ubb.fmi.orar.feature.settings.viewmodel.ThemeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Provides the Koin module for settings feature.
 * This module includes the ThemeViewModel for managing settings-related data.
 */
fun settingsFeatureModule() = module {
    viewModelOf(::ThemeViewModel)
}
