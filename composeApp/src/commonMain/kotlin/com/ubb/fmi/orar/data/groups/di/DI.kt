package com.ubb.fmi.orar.data.groups.di

import com.ubb.fmi.orar.data.groups.api.StudyLinesApi
import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSourceImpl
import org.koin.dsl.module

fun studyLinesDataModule() = module {
    factory<StudyLinesApi> { StudyLinesApi(get()) }
    factory<StudyLinesDataSource> { StudyLinesDataSourceImpl(get(), get(), get()) }
}
