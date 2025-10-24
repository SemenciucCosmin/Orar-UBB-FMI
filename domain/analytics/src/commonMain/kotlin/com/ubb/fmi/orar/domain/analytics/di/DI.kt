package com.ubb.fmi.orar.domain.analytics.di

import com.ubb.fmi.orar.domain.analytics.AnalyticsLogger
import com.ubb.fmi.orar.domain.analytics.AnalyticsLoggerImpl
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for analytics logging domain operations.
 */
fun analyticsDomainModule() = module {
    factory<AnalyticsLogger> { AnalyticsLoggerImpl() }
}