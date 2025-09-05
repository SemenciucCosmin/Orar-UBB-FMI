package com.ubb.fmi.orar.data.teachers.di

import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSourceImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for teachers data operations.
 * This module includes the TeachersDataSource for managing teacher-related data.
 */
fun teachersDataModule() = module {
    factory<TeachersDataSource> { TeachersDataSourceImpl(get(), get(), get(), get()) }
}
