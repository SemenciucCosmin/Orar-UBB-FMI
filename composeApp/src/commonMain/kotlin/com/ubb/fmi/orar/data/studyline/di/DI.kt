package com.ubb.fmi.orar.data.studyline.di

import com.ubb.fmi.orar.data.studyline.api.StudyLineApi
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSourceImpl
import org.koin.dsl.module

fun studyLineDataModule() = module {
    factory<StudyLineApi> { StudyLineApi(get()) }
    factory<StudyLineDataSource> { StudyLineDataSourceImpl(get(), get()) }
}
