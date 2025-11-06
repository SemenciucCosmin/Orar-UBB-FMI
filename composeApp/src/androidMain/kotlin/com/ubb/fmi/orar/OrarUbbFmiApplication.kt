package com.ubb.fmi.orar

import android.app.Application
import com.ubb.fmi.orar.app.AppInitializer
import com.ubb.fmi.orar.di.KoinInitializer
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.koin.android.ext.koin.androidContext
import org.koin.core.component.KoinComponent

/**
 * Main application class for the Orar UBB FMI application.
 * This class initializes Koin for dependency injection.
 */
class OrarUbbFmiApplication : Application(), KoinComponent {

    override fun onCreate() {
        super.onCreate()
        KoinInitializer.initKoin {
            androidContext(this@OrarUbbFmiApplication)
        }

        val firebaseOptions = FirebaseOptions(
            applicationId = APPLICATION_ID,
            apiKey = API_KEY,
            projectId = PROJECT_ID,
            storageBucket = STORAGE_BUCKET
        )

        if (!BuildConfig.DEBUG) {
            Firebase.initialize(this@OrarUbbFmiApplication, firebaseOptions, APP_NAME)
        }

        AppInitializer().initApp()
    }

    companion object {
        private const val APP_NAME = "Orar-UBB-FMI"
        private const val APPLICATION_ID = "1:737974025604:android:92bf4280b1c0c0094e31d1"
        private const val API_KEY = "AIzaSyAYcfKA21BQ-5HvqlfxoD0kYdDj_q29aVU"
        private const val PROJECT_ID = "orar-ubb-fmi"
        private const val STORAGE_BUCKET = "orar-ubb-fmi.firebasestorage.app"
    }
}
