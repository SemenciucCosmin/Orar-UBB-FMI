package com.example.orarubb_fmi.di

import android.content.Context
import com.example.orarubb_fmi.di.modules.libraryModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.dsl.koinApplication

object KoinContext {

    private lateinit var koinApp: KoinApplication

    internal val koin: Koin
        get() = koinApp.koin

    fun startKoin(context: Context) {
        if (::koinApp.isInitialized) return

        koinApp = koinApplication {
            androidContext(context)
            modules(libraryModules)
        }
    }
}
