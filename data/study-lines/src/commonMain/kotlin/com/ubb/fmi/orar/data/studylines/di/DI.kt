package com.ubb.fmi.orar.data.studylines.di

import com.ubb.fmi.orar.data.network.service.StudyLinesApi
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSourceImpl
import org.koin.dsl.module

fun studyLinesDataModule() = module {
    factory<StudyLinesDataSource> { StudyLinesDataSourceImpl(get(), get(), get()) }
}
