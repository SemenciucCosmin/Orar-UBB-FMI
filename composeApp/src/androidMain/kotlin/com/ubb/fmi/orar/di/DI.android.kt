package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.data.database.DatabaseFactory
import com.ubb.fmi.orar.data.preferences.DataStoreFactory
import com.ubb.fmi.orar.domain.logging.AndroidLogger
import com.ubb.fmi.orar.domain.logging.Logger
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { OkHttp.create() }
    single { DatabaseFactory(androidApplication()) }
    single { DataStoreFactory(androidApplication()) }
    factory<Logger> { AndroidLogger() }
}
