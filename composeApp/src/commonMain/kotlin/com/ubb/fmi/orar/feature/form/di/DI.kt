package com.ubb.fmi.orar.feature.form.di

import com.ubb.fmi.orar.feature.form.viewmodel.OnboardingFormViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

fun formFeatureModule() = module {
    viewModelOf(::OnboardingFormViewModel)
}
