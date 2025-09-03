package com.ubb.fmi.orar.domain.usertimetable.di

import com.ubb.fmi.orar.domain.usertimetable.usecase.GetUserTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun userTimetableDomainModule() = module {
    factoryOf(::GetUserTimetableUseCase)
}
