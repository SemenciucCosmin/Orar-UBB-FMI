package com.ubb.fmi.orar.data.database.di

import com.ubb.fmi.orar.data.database.DatabaseFactory
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for database operations.
 * This module includes the DatabaseFactory and DAO instances for database access.
 */
actual fun platformModule() = module {
    single { DatabaseFactory() }
}