package com.ubb.fmi.orar.data.network.di

import io.ktor.client.engine.HttpClientEngine
import org.koin.dsl.module

actual fun platformModule() = module {
    single<HttpClientEngine> { Darwin.create() }
}