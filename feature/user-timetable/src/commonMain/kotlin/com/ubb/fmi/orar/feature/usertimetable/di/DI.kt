package com.ubb.fmi.orar.feature.usertimetable.di

import com.ubb.fmi.orar.feature.usertimetable.ui.viewmodel.UserTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for User Timetable feature.
 *
 * This module provides the ViewModel for the User Timetable screen.
 */
fun userTimetableFeatureModule() = module {
    viewModelOf(::UserTimetableViewModel)
}
