package com.ubb.fmi.orar.feature.roomtimetable.di

import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Room Timetable feature.
 * This module provides the ViewModel for the Room Timetable screen.
 */
fun roomTimetableFeatureModule() = module {
    viewModelOf(::RoomTimetableViewModel)
}
