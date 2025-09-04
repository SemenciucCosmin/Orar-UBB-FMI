package com.ubb.fmi.orar.di

/**
 * Represents the platform on which the application is running.
 */
interface Platform {
    val name: String
}

/**
 * Provides the platform-specific implementation for the current platform.
 */
expect fun getPlatform(): Platform