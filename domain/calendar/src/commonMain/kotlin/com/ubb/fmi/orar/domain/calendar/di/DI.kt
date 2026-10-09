package com.ubb.fmi.orar.domain.calendar.di

import com.ubb.fmi.orar.domain.calendar.usecase.GetAcademicYearUseCase
import com.ubb.fmi.orar.domain.calendar.usecase.GetUpcomingEventOccurrencesUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the Koin module for academic year calendar domain operations.
 */
fun calendarDomainModule() = module {
    factoryOf(::GetAcademicYearUseCase)
    factoryOf(::GetUpcomingEventOccurrencesUseCase)
}
