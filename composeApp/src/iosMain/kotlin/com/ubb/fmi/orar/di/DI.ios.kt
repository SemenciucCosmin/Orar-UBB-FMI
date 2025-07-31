package com.ubb.fmi.orar.di

import com.ubb.fmi.orar.logging.IosLogger
import com.ubb.fmi.orar.logging.Logger
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { Darwin.create() }
    factory<Logger> { IosLogger() }
}
