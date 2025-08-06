package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.data.database.DatabaseFactory
import com.ubb.fmi.orar.data.preferences.DataStoreFactory
import com.ubb.fmi.orar.logging.IosLogger
import com.ubb.fmi.orar.logging.Logger
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { Darwin.create() }
    single { DatabaseFactory() }
    single { DataStoreFactory() }
    factory<Logger> { IosLogger() }
}
