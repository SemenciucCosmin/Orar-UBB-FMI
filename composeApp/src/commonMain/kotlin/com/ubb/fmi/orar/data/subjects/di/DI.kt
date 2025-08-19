package com.ubb.fmi.orar.data.subjects.di

import com.ubb.fmi.orar.data.subjects.api.SubjectsApi
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSourceImpl
import org.koin.dsl.module

fun subjectsDataModule() = module {
    factory<SubjectsApi> { SubjectsApi(get()) }
    factory<SubjectsDataSource> { SubjectsDataSourceImpl(get(), get(), get()) }
}