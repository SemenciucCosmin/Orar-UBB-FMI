package com.ubb.fmi.orar.data.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ubb.fmi.orar.data.database.DatabaseFactory
import com.ubb.fmi.orar.data.database.OrarUbbFmiDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for database operations.
 * This module includes the DatabaseFactory and DAO instances for database access.
 */
expect fun platformModule(): Module

/**
 * Koin module for providing database-related dependencies.
 * This module includes the DatabaseFactory and DAO instances for database access.
 */
fun databaseDataModule() = module {
    includes(platformModule())
    single { get<DatabaseFactory>().create().setDriver(BundledSQLiteDriver()).build() }
    single { get<OrarUbbFmiDatabase>().eventDao }
    single { get<OrarUbbFmiDatabase>().groupDao }
    single { get<OrarUbbFmiDatabase>().roomDao }
    single { get<OrarUbbFmiDatabase>().studyLineDao }
    single { get<OrarUbbFmiDatabase>().subjectDao }
    single { get<OrarUbbFmiDatabase>().teacherDao }
}
