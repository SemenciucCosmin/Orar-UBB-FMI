package com.ubb.fmi.orar.domain.usertimetable.di

import com.ubb.fmi.orar.domain.usertimetable.usecase.AddCustomEventsUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.AdoptEventUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetCurrentWeekUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.IsConfigurationDoneUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.RefreshUserTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

/**
 * Provides the Koin module for user timetable domain operations.
 * This module includes the use case for retrieving the user's timetable.
 */
fun userTimetableDomainModule() = module {
    factoryOf(::GetUserTimetableUseCase)
    factoryOf(::GetCurrentWeekUseCase)
    factoryOf(::AddCustomEventsUseCase)
    factoryOf(::AdoptEventUseCase)
    factoryOf(::IsConfigurationDoneUseCase)
    factoryOf(::RefreshUserTimetableUseCase)
}
