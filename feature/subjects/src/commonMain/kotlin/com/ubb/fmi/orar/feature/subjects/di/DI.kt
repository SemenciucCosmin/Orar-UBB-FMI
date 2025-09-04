package com.ubb.fmi.orar.feature.subjects.di

import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.SubjectsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Subjects feature.
 */
fun subjectsFeatureModule() = module {
    viewModelOf(::SubjectsViewModel)
}
