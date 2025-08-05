package com.ubb.fmi.orar.data.teachers.di

import com.ubb.fmi.orar.data.teachers.api.TeachersApi
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSourceImpl
import org.koin.dsl.module

fun teachersDataModule() = module {
    factory<TeachersApi> { TeachersApi(get()) }
    factory<TeachersDataSource> { TeachersDataSourceImpl(get(), get()) }
}
