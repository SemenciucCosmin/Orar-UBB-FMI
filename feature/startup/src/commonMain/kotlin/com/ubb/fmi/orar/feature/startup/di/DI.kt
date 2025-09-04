package com.ubb.fmi.orar.feature.startup.di

import com.ubb.fmi.orar.feature.startup.ui.viewmodel.StartupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Startup feature.
 *
 * This module provides the necessary dependencies for the StartupViewModel.
 */
fun startupFeatureModule() = module {
    viewModelOf(::StartupViewModel)
}
