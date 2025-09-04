package com.ubb.fmi.orar.feature.teachers.di

import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.TeachersViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Teachers feature.
 */
fun teachersFeatureModule() = module {
    viewModelOf(::TeachersViewModel)
}
