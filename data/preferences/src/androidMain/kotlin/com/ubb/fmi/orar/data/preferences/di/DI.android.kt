package com.ubb.fmi.orar.data.preferences.di

import com.ubb.fmi.orar.data.preferences.DataStoreFactory
import org.koin.dsl.module

actual fun preferencesDataModule() = module {
    single { DataStoreFactory(get()) }
}
