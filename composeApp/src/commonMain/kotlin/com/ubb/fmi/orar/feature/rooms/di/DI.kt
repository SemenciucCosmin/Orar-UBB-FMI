package com.ubb.fmi.orar.feature.rooms.di

import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.RoomsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun roomsFeatureModule() = module {
    viewModelOf(::RoomsViewModel)
}
