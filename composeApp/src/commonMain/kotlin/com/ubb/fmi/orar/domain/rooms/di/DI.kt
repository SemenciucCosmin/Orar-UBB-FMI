package com.ubb.fmi.orar.domain.rooms.di

import com.ubb.fmi.orar.domain.rooms.usecase.GetRoomTimetableUseCase
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

fun roomsDomainModule() = module {
    factoryOf(::GetRoomTimetableUseCase)
}
