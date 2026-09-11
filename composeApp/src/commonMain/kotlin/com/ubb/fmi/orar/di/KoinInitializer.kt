package com.ubb.fmi.orar.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.mp.KoinPlatformTools

/**
 * Initializes Koin for dependency injection in the Orar UBB FMI application.
 * This function sets up the Koin context with the provided configuration and modules.
 */
object KoinInitializer {

    /**
     * Initializes Koin with the provided configuration and common modules.
     * Safe to call multiple times: if Koin is already started (e.g. the iOS view
     * controller gets recreated for a new deep link), this is a no-op instead of
     * throwing [org.koin.core.error.KoinApplicationAlreadyStartedException].
     *
     * @param config Optional KoinAppDeclaration to customize the Koin setup.
     */
    fun initKoin(config: KoinAppDeclaration? = null) {
        if (KoinPlatformTools.defaultContext().getOrNull() != null) return

        startKoin {
            config?.invoke(this)
            modules(
                commonModule()
            )
        }
    }
}