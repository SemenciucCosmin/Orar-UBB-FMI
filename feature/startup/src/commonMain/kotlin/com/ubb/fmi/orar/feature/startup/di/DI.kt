package com.ubb.fmi.orar.feature.startup.di

import com.ubb.fmi.orar.feature.startup.ui.viewmodel.StartupViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun startupFeatureModule() = module {
    viewModelOf(::StartupViewModel)
}
