package com.ubb.fmi.orar.domain.feedback.di

import com.ubb.fmi.orar.domain.feedback.usecase.GetFeedbackLoopReadinessUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.IncreaseAppUsagePointsUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.PostponeFeedbackLoopUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.SetFeedbackShownUseCase
import com.ubb.fmi.orar.domain.feedback.usecase.SetFirstUsageTimestampUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for feedback domain layer
 */
fun feedbackDomainModule() = module {
    factoryOf(::SetFeedbackShownUseCase)
    factoryOf(::IncreaseAppUsagePointsUseCase)
    factoryOf(::SetFirstUsageTimestampUseCase)
    factoryOf(::PostponeFeedbackLoopUseCase)
    factoryOf(::GetFeedbackLoopReadinessUseCase)
}
