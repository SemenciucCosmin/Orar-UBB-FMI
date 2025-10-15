package com.ubb.fmi.orar.data.preferences.di

import com.ubb.fmi.orar.data.preferences.factory.DataStoreFactory
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for preferences data operations.
 * This module includes the DataStoreFactory for managing preferences.
 */
actual fun preferencesDataModule() = module {
    single { DataStoreFactory(get()) }
}
