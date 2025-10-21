package com.ubb.fmi.orar.domain.timetable.di

import com.ubb.fmi.orar.domain.timetable.usecase.ChangeEventVisibilityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedNewsDataValidityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedTimetableDataValidityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.DeletePersonalEventUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.InvalidateCachedDataUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the Koin module for timetable domain operations.
 * This module includes use cases for managing timetable configurations and class visibility.
 */
fun timetableDomainModule() = module {
    factoryOf(::ChangeEventVisibilityUseCase)
    factoryOf(::DeletePersonalEventUseCase)
    factoryOf(::CheckCachedTimetableDataValidityUseCase)
    factoryOf(::CheckCachedNewsDataValidityUseCase)
    factoryOf(::InvalidateCachedDataUseCase)
}
