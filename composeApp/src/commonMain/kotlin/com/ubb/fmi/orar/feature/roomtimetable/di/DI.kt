package com.ubb.fmi.orar.feature.roomtimetable.di

import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun roomTimetableFeatureModule() = module {
    viewModelOf(::RoomTimetableViewModel)
}
