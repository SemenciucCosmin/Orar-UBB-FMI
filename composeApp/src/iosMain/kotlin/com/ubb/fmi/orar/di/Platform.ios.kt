@file:Suppress("MatchingDeclarationName")

package com.ubb.fmi.orar.di

import platform.UIKit.UIDevice

/**
 * Represents the platform on which the application is running.
 */
class IosPlatform : Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
}

/**
 * Provides the platform-specific implementation for iOS.
 */
actual fun getPlatform(): Platform = IosPlatform()