package com.ubb.fmi.orar.data.subjects.di

import com.ubb.fmi.orar.data.network.service.SubjectsApi
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSourceImpl
import org.koin.dsl.module

fun subjectsDataModule() = module {
    factory<SubjectsDataSource> { SubjectsDataSourceImpl(get(), get(), get()) }
}