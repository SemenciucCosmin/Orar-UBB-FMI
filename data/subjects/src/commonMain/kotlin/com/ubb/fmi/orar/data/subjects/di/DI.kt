package com.ubb.fmi.orar.data.subjects.di

import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSourceImpl
import com.ubb.fmi.orar.data.subjects.repository.SubjectsRepository
import com.ubb.fmi.orar.data.subjects.repository.SubjectsRepositoryImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for subjects data layer.
 */
fun subjectsDataModule() = module {
    factory<SubjectsDataSource> { SubjectsDataSourceImpl(get(), get(), get(), get()) }
    single<SubjectsRepository> { SubjectsRepositoryImpl(get(), get(), get(), get()) }
}