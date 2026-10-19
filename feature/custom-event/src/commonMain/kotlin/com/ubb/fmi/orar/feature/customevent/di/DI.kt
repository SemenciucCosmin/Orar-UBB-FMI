package com.ubb.fmi.orar.feature.customevent.di

import com.ubb.fmi.orar.feature.customevent.ui.viewmodel.AddCustomEventViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun customEventFeatureModule() = module {
    viewModelOf(::AddCustomEventViewModel)
}
