package com.ubb.fmi.orar.feature.subjecttimetable.di

import com.ubb.fmi.orar.feature.subjecttimetable.ui.viewmodel.SubjectTimetableViewModel
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.TeacherTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun subjectTimetableFeatureModule() = module {
    viewModelOf(::SubjectTimetableViewModel)
}
