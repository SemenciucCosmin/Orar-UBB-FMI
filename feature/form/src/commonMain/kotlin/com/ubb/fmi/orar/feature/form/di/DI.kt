package com.ubb.fmi.orar.feature.form.di

import com.ubb.fmi.orar.feature.form.ui.viewmodel.GroupsFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.OnboardingFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.TeachersFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Koin module for the Form feature.
 * This module provides the ViewModels for the Form screens.
 */
fun formFeatureModule() = module {
    viewModelOf(::OnboardingFormViewModel)
    viewModelOf(::TeachersFormViewModel)
    viewModelOf(::StudyLinesFormViewModel)
    viewModelOf(::GroupsFormViewModel)
}
