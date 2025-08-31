package com.ubb.fmi.orar.domain.timetable.di

import com.ubb.fmi.orar.domain.timetable.usecase.ChangeTimetableClassVisibilityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.CheckCachedDataValidityUseCase
import com.ubb.fmi.orar.domain.timetable.usecase.SetConfigurationUseCase
import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun timetableDomainModule() = module {
    factoryOf(::ChangeTimetableClassVisibilityUseCase)
    factoryOf(::SetConfigurationUseCase)
    factoryOf(::CheckCachedDataValidityUseCase)
}
