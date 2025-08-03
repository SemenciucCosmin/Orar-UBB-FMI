package com.ubb.fmi.orar.data.timetables.di

import com.ubb.fmi.orar.data.timetables.repository.TimetablesRepository
import com.ubb.fmi.orar.data.timetables.repository.TimetablesRepositoryImpl
import org.koin.dsl.module

fun timetablesDataModule() = module {
    factory<TimetablesRepository> { TimetablesRepositoryImpl(get(), get(), get(), get()) }
}
