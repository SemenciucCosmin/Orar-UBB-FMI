package com.ubb.fmi.orar.data.groups.di

import com.ubb.fmi.orar.data.groups.datasource.GroupsDataSource
import com.ubb.fmi.orar.data.groups.datasource.GroupsDataSourceImpl
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.groups.repository.GroupsRepositoryImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for group data layer.
 */
fun groupsDataModule() = module {
    factory<GroupsDataSource> { GroupsDataSourceImpl(get(), get(), get(), get()) }
    factory<GroupsRepository> { GroupsRepositoryImpl(get(), get(), get(), get(), get()) }
}
