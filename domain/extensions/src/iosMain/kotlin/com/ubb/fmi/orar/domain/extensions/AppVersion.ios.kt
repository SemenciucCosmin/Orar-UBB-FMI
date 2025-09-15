package com.ubb.fmi.orar.domain.extensions

import platform.Foundation.NSBundle

/**
 * Platform specific implementation for getting the current app version
 */
actual fun getAppVersion(platformContext: Any?): String? {
    return NSBundle.mainBundle.infoDictionary?.get("CFBundleShortVersionString") as? String
}