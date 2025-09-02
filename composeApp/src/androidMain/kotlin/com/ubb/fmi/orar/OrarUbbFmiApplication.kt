package com.ubb.fmi.orar

import android.app.Application
import com.ubb.fmi.orar.di.KoinInitializer
import org.koin.android.ext.koin.androidContext

class OrarUbbFmiApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer.initKoin {
            androidContext(this@OrarUbbFmiApplication)
        }
    }
}
