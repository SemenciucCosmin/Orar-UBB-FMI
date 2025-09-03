package com.ubb.fmi.orar.domain.logging.di

import Logger
import com.ubb.fmi.orar.domain.logging.IosLogger
import org.koin.dsl.module

actual fun loggingDomainModule() = module {
    factory<Logger> { IosLogger() }
}