package com.ubb.fmi.orar.feature.form.di

import com.ubb.fmi.orar.feature.form.viewmodel.OnboardingFormViewModel
import com.ubb.fmi.orar.feature.form.viewmodel.StudyGroupsFormViewModel
import com.ubb.fmi.orar.feature.form.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.viewmodel.TeachersFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun formFeatureModule() = module {
    viewModelOf(::OnboardingFormViewModel)
    viewModelOf(::TeachersFormViewModel)
    viewModelOf(::StudyLinesFormViewModel)
    viewModelOf(::StudyGroupsFormViewModel)
}
