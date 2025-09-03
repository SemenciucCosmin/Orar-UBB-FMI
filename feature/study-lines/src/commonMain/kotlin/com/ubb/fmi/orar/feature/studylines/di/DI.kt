package com.ubb.fmi.orar.feature.studylines.di

import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.StudyLinesViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun studyLinesFeatureModule() = module {
    viewModelOf(::StudyLinesViewModel)
}
