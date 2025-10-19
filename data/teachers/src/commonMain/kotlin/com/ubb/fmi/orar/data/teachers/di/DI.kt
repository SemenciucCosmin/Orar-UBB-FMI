package com.ubb.fmi.orar.data.teachers.di

import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSourceImpl
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepository
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepositoryImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for teachers data layer.
 */
fun teachersDataModule() = module {
    factory<TeachersDataSource> { TeachersDataSourceImpl(get(), get(), get(), get()) }
    single<TeacherRepository> { TeacherRepositoryImpl(get(), get(), get(), get()) }
}
