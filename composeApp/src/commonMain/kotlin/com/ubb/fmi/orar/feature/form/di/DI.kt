package com.ubb.fmi.orar.feature.form.di

import com.ubb.fmi.orar.feature.form.ui.viewmodel.OnboardingFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyGroupsFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.TeachersFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun formFeatureModule() = module {
    viewModelOf(::OnboardingFormViewModel)
    viewModelOf(::TeachersFormViewModel)
    viewModelOf(::StudyLinesFormViewModel)
    viewModelOf(::StudyGroupsFormViewModel)
}
