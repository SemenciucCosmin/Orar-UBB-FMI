package com.ubb.fmi.orar.data.preferences.di

import com.ubb.fmi.orar.data.preferences.DataStoreFactory
import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.data.preferences.TimetablePreferencesImpl
import org.koin.dsl.module

fun preferencesDataModule() = module {
    single<TimetablePreferences> { TimetablePreferencesImpl(get<DataStoreFactory>().create()) }
}
