package com.ubb.fmi.orar.feature.settings.di

import com.ubb.fmi.orar.feature.settings.viewmodel.AboutViewModel
import com.ubb.fmi.orar.feature.settings.viewmodel.ColorPickerViewModel
import com.ubb.fmi.orar.feature.settings.viewmodel.ColorsViewModel
import com.ubb.fmi.orar.feature.settings.viewmodel.ThemeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Provides the Koin module for settings feature.
 * This module includes the view models for managing settings-related data.
 */
fun settingsFeatureModule() = module {
    viewModelOf(::ThemeViewModel)
    viewModelOf(::AboutViewModel)
    viewModelOf(::ColorsViewModel)
    viewModelOf(::ColorPickerViewModel)
}
