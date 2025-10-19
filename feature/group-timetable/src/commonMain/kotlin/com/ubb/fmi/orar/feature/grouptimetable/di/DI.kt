package com.ubb.fmi.orar.feature.grouptimetable.di

import com.ubb.fmi.orar.feature.grouptimetable.ui.viewmodel.GroupTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Study Line Timetable feature.
 *
 * This module provides the ViewModel for the Study Line Timetable screen.
 */
fun groupsTimetableFeatureModule() = module {
    viewModelOf(::GroupTimetableViewModel)
}
