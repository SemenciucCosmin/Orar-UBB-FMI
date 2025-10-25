package com.ubb.fmi.orar.feature.feedback.ui.di

import com.ubb.fmi.orar.feature.feedback.ui.viewmodel.FeedbackViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for feedback feature layer
 */
fun feedbackFeatureModule() = module {
    viewModelOf(::FeedbackViewModel)
}