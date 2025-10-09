package com.ubb.fmi.orar.data.timetable.di

import com.ubb.fmi.orar.data.preferences.DataStoreFactory
import com.ubb.fmi.orar.data.timetable.datasource.TimetableClassDataSource
import com.ubb.fmi.orar.data.timetable.datasource.TimetableClassDataSourceImpl
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferencesImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for timetable data operations.
 * This module includes the TimetablePreferences for managing timetable-related preferences.
 */
fun timetableDataModule() = module {
    factory<TimetableClassDataSource> { TimetableClassDataSourceImpl(get(), get()) }
    single<TimetablePreferences> {
        TimetablePreferencesImpl(
            get<DataStoreFactory>().create(TimetablePreferences.PREFERENCES_NAME)
        )
    }
}
