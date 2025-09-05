package com.ubb.fmi.orar.data.rooms.di

import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSourceImpl
import org.koin.dsl.module

/**
 * Provides the Koin module for rooms data operations.
 * This module includes the RoomsDataSource for managing room-related data.
 */
fun roomsDataModule() = module {
    factory<RoomsDataSource> { RoomsDataSourceImpl(get(), get(), get(), get()) }
}
