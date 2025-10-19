package com.ubb.fmi.orar.data.studylines.di

import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSourceImpl
import com.ubb.fmi.orar.data.studylines.repository.StudyLinesRepository
import com.ubb.fmi.orar.data.studylines.repository.StudyLinesRepositoryImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for study lines data layer.
 */
fun studyLinesDataModule() = module {
    factory<StudyLinesDataSource> { StudyLinesDataSourceImpl(get(), get(), get()) }
    single<StudyLinesRepository> { StudyLinesRepositoryImpl(get(), get(), get()) }
}
