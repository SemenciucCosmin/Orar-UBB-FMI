package com.ubb.fmi.orar.feature.teachertimetable.di

import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.TeacherTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun teacherTimetableFeatureModule() = module {
    viewModelOf(::TeacherTimetableViewModel)
}
