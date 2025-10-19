package com.ubb.fmi.orar.domain.usertimetable.di

import com.ubb.fmi.orar.domain.usertimetable.usecase.AddPersonalEventUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the Koin module for user timetable domain operations.
 * This module includes the use case for retrieving the user's timetable.
 */
fun userTimetableDomainModule() = module {
    factoryOf(::GetUserTimetableUseCase)
    factoryOf(::GetCurrentWeekUseCase)
    factoryOf(::AddPersonalEventUseCase)
}
