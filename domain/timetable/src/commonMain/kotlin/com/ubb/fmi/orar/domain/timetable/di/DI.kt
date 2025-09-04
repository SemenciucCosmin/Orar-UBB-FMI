package com.ubb.fmi.orar.domain.timetable.di

import com.ubb.fmi.orar.domain.timetable.usecase.ChangeTimetableClassVisibilityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedDataValidityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.SetTimetableConfigurationUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the Koin module for timetable domain operations.
 * This module includes use cases for managing timetable configurations and class visibility.
 */
fun timetableDomainModule() = module {
    factoryOf(::ChangeTimetableClassVisibilityUseCase)
    factoryOf(::SetTimetableConfigurationUseCase)
    factoryOf(::CheckCachedDataValidityUseCase)
}
