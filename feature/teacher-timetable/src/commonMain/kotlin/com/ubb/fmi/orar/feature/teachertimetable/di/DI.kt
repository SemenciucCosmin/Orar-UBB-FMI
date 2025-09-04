package com.ubb.fmi.orar.feature.teachertimetable.di

import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.TeacherTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Teacher Timetable feature.
 *
 * This module provides the ViewModel for the Teacher Timetable screen.
 */
fun teacherTimetableFeatureModule() = module {
    viewModelOf(::TeacherTimetableViewModel)
}
