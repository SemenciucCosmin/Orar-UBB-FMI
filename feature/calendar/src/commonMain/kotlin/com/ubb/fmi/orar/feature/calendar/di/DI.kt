package com.ubb.fmi.orar.feature.calendar.di

import com.ubb.fmi.orar.feature.calendar.ui.viewmodel.CalendarViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the calendar feature.
 */
fun calendarFeatureModule() = module {
    viewModelOf(::CalendarViewModel)
}
