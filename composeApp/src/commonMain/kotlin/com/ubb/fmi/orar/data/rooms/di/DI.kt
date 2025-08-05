package com.ubb.fmi.orar.data.rooms.di

import com.ubb.fmi.orar.data.rooms.api.RoomsApi
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSourceImpl
import org.koin.dsl.module

fun roomsDataModule() = module {
    factory<RoomsApi> { RoomsApi(get()) }
    factory<RoomsDataSource> { RoomsDataSourceImpl(get(), get()) }
}
