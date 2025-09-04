package com.ubb.fmi.orar.data.network.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin
import org.koin.dsl.module

/**
 * Provides the platform-specific Koin module for network operations.
 * This module includes the HTTP client engine for making network requests.
 */
actual fun platformModule() = module {
    single<HttpClientEngine> { Darwin.create() }
}