package com.ubb.fmi.orar.feature.freerooms.di

import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.FreeRoomsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Free Rooms Search feature.
 */
fun freeRoomsFeatureModule() = module {
    viewModelOf(::FreeRoomsViewModel)
}
