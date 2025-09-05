package com.ubb.fmi.orar.data.subjects.di

import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSourceImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for subjects data operations.
 * This module includes the SubjectsDataSource for managing subject-related data.
 */
fun subjectsDataModule() = module {
    factory<SubjectsDataSource> { SubjectsDataSourceImpl(get(), get(), get(), get()) }
}