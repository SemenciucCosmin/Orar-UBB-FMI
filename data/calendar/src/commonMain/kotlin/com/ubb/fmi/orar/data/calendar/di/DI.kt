package com.ubb.fmi.orar.data.calendar.di

import com.ubb.fmi.orar.data.calendar.datasource.AcademicCalendarDataSource
import com.ubb.fmi.orar.data.calendar.datasource.AcademicCalendarDataSourceImpl
import com.ubb.fmi.orar.data.calendar.repository.AcademicCalendarRepository
import com.ubb.fmi.orar.data.calendar.repository.AcademicCalendarRepositoryImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for academic calendar data layer.
 */
fun calendarDataModule() = module {
    factory<AcademicCalendarDataSource> { AcademicCalendarDataSourceImpl(get(), get(), get()) }
    single<AcademicCalendarRepository> { AcademicCalendarRepositoryImpl(get(), get(), get()) }
}
