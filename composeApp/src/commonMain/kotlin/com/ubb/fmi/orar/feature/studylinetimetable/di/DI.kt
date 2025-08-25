package com.ubb.fmi.orar.feature.studylinetimetable.di

import com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel.StudyLineTimetableViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun studyLineTimetableFeatureModule() = module {
    viewModelOf(::StudyLineTimetableViewModel)
}
