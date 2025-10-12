package com.ubb.fmi.orar.data.students.di

import com.ubb.fmi.orar.data.students.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.students.datasource.GroupsDataSourceImpl
import com.ubb.fmi.orar.data.students.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.students.datasource.StudyLinesDataSourceImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for study lines data operations.
 * This module includes the StudyLinesDataSource for managing study line-related data.
 */
fun studentsDataModule() = module {
    factory<StudyLinesDataSource> { StudyLinesDataSourceImpl(get(), get(), get()) }
    factory<GroupsDataSource> { GroupsDataSourceImpl(get(), get(), get(), get(), get(), get()) }
}
