package com.ubb.fmi.orar.data.database.di

import com.ubb.fmi.orar.data.database.DatabaseFactory
import org.koin.dsl.module

actual fun platformModule() = module {
    single { DatabaseFactory() }
}