package com.ubb.fmi.orar.domain.extensions

import android.content.Context
import android.content.pm.PackageManager

/**
 * Platform specific implementation for getting the current app version
 */
actual fun getAppVersion(platformContext: Any?): String? {
    val context = platformContext as? Context ?: return null
    return try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: PackageManager.NameNotFoundException) {
        null
    }
}