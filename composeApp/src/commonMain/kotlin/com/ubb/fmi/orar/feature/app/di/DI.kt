package com.ubb.fmi.orar.feature.app.di

import com.ubb.fmi.orar.feature.app.viewmodel.AppViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun appFeatureModule() = module {
    viewModelOf(::AppViewModel)
}
