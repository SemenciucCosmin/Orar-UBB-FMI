package com.ubb.fmi.orar.feature.studylinetimetable.di

import com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel.StudyLineTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Study Line Timetable feature.
 *
 * This module provides the ViewModel for the Study Line Timetable screen.
 */
fun studyLineTimetableFeatureModule() = module {
    viewModelOf(::StudyLineTimetableViewModel)
}
