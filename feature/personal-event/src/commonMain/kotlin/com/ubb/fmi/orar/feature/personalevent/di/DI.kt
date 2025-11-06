package com.ubb.fmi.orar.feature.personalevent.di

import com.ubb.fmi.orar.feature.personalevent.ui.viewmodel.AddPersonalEventViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun personalEventFeatureModule() = module {
    viewModelOf(::AddPersonalEventViewModel)
}
