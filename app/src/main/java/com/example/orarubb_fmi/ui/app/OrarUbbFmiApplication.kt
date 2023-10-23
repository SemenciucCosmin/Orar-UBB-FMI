package com.example.orarubb_fmi.ui.app

import android.app.Application
import com.example.orarubb_fmi.di.modules.libraryModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class OrarUbbFmiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@OrarUbbFmiApplication)
            modules(libraryModules)
        }
    }
}
