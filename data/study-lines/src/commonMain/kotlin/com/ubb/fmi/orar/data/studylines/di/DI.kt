package com.ubb.fmi.orar.data.students.di

import com.ubb.fmi.orar.data.students.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.students.datasource.GroupsDataSourceImpl
import com.ubb.fmi.orar.data.students.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.students.datasource.StudyLinesDataSourceImpl
import com.ubb.fmi.orar.data.students.repository.GroupsRepository
import com.ubb.fmi.orar.data.students.repository.GroupsRepositoryImpl
import com.ubb.fmi.orar.data.students.repository.StudyLinesRepository
import com.ubb.fmi.orar.data.students.repository.StudyLinesRepositoryImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for study lines data operations.
 * This module includes the StudyLinesDataSource for managing study line-related data.
 */
fun studyLinesDataModule() = module {
    factory<StudyLinesDataSource> { StudyLinesDataSourceImpl(get(), get(), get()) }
    single<StudyLinesRepository> { StudyLinesRepositoryImpl(get(), get(), get()) }
}
