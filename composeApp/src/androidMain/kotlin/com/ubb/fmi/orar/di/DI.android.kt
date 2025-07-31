package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.logging.AndroidLogger
import com.ubb.fmi.orar.logging.Logger
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { OkHttp.create() }
    factory<Logger> { AndroidLogger() }
}
