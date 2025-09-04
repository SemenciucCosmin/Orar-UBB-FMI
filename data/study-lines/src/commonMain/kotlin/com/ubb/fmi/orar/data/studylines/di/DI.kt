package com.ubb.fmi.orar.data.studylines.di

import com.ubb.fmi.orar.data.network.service.StudyLinesApi
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSourceImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for study lines data operations.
 * This module includes the StudyLinesDataSource for managing study line-related data.
 */
fun studyLinesDataModule() = module {
    factory<StudyLinesDataSource> { StudyLinesDataSourceImpl(get(), get(), get()) }
}
