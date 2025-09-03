package com.ubb.fmi.orar.data.teachers.di

import com.ubb.fmi.orar.data.network.service.TeachersApi
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSourceImpl
import org.koin.dsl.module

fun teachersDataModule() = module {
    factory<TeachersDataSource> { TeachersDataSourceImpl(get(), get(), get()) }
}
