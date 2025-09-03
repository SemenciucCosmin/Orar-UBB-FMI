package com.ubb.fmi.orar.data.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.ubb.fmi.orar.data.database.DatabaseFactory
import com.ubb.fmi.orar.data.database.OrarUbbFmiDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect fun platformModule(): Module

fun databaseDataModule() = module {
    includes(platformModule())
    single { get<DatabaseFactory>().create().setDriver(BundledSQLiteDriver()).build() }
    single { get<OrarUbbFmiDatabase>().roomDao }
    single { get<OrarUbbFmiDatabase>().studyLineDao }
    single { get<OrarUbbFmiDatabase>().subjectDao }
    single { get<OrarUbbFmiDatabase>().teacherDao }
    single { get<OrarUbbFmiDatabase>().timetableClassDao }
}
