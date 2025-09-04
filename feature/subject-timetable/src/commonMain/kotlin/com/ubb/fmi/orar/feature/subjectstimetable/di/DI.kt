package com.ubb.fmi.orar.feature.subjectstimetable.di

import com.ubb.fmi.orar.feature.subjectstimetable.ui.viewmodel.SubjectTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Subject Timetable feature.
 */
fun subjectTimetableFeatureModule() = module {
    viewModelOf(::SubjectTimetableViewModel)
}
