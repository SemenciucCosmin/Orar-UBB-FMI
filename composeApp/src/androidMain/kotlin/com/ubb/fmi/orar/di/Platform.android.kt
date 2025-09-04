@file:Suppress("MatchingDeclarationName")

package com.ubb.fmi.orar.di

import android.os.Build

/**
 * Represents the platform on which the application is running.
 */
class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

/**
 * Provides the platform-specific implementation for Android.
 */
actual fun getPlatform(): Platform = AndroidPlatform()