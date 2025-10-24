package com.ubb.fmi.orar.feature.dialogs.ui.di

import com.ubb.fmi.orar.feature.dialogs.ui.viewmodel.DialogsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for dialogs feature layer
 */
fun dialogsFeatureModule() = module {
    viewModelOf(::DialogsViewModel)
}