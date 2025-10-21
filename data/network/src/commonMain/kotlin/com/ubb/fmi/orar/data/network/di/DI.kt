package com.ubb.fmi.orar.data.network.di

import com.ubb.fmi.orar.data.network.service.NewsApi
import com.ubb.fmi.orar.data.network.service.RoomsApi
import com.ubb.fmi.orar.data.network.service.StudentsApi
import com.ubb.fmi.orar.data.network.service.SubjectsApi
import com.ubb.fmi.orar.data.network.service.TeachersApi
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.dsl.module

private const val TIMEOUT_MILLIS = 20_000L

/**
 * Provides the platform-specific Koin module for network operations.
 * This module includes the HTTP client and API service instances for making network requests.
 */
expect fun platformModule(): Module

/**
 * Koin module for providing network-related dependencies.
 * This module includes the HTTP client and API service instances for making network requests.
 */
fun networkDataModule() = module {
    includes(platformModule())
    single<HttpClient> {
        HttpClient(engine = get()) {
            install(ContentNegotiation) {
                json(Json { ignoreUnknownKeys = true })
            }

            install(HttpTimeout) {
                socketTimeoutMillis = TIMEOUT_MILLIS
                requestTimeoutMillis = TIMEOUT_MILLIS
            }

            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
                        println(message)
                    }
                }
            }

            defaultRequest {
                contentType(ContentType.Application.Json)
            }
        }
    }

    factory<NewsApi> { NewsApi(get()) }
    factory<RoomsApi> { RoomsApi(get()) }
    factory<StudentsApi> { StudentsApi(get()) }
    factory<SubjectsApi> { SubjectsApi(get()) }
    factory<TeachersApi> { TeachersApi(get()) }
}
