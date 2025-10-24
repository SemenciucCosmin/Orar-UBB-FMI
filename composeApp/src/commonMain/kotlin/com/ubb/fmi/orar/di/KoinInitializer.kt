package com.ubb.fmi.orar.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

/**
 * Initializes Koin for dependency injection in the Orar UBB FMI application.
 * This function sets up the Koin context with the provided configuration and modules.
 */
object KoinInitializer {

    /**
     * Initializes Koin with the provided configuration and common modules.
     *
     * @param config Optional KoinAppDeclaration to customize the Koin setup.
     */
    fun initKoin(config: KoinAppDeclaration? = null) {
        startKoin {
            config?.invoke(this)
            modules(
                commonModule()
            )
        }
    }
}