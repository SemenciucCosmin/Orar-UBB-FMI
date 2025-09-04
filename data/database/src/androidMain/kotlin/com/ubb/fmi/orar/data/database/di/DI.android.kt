package com.ubb.fmi.orar.data.database.di

import com.ubb.fmi.orar.data.database.DatabaseFactory
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for Android.
 * This module includes the DatabaseFactory for database operations.
 */
actual fun platformModule() = module {
    single { DatabaseFactory(get()) }
}
