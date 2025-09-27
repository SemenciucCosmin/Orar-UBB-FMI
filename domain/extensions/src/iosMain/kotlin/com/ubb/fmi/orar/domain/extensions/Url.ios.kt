package com.ubb.fmi.orar.domain.extensions

import platform.Foundation.NSURL
import platform.UIKit.UIApplication

/**
 * Opens the given [url] in the system’s default browser.
 * Uses [UIApplication.openURL].
 * @param url The URL to open (must be absolute).
 * @param platformContext Optional context, required only on Android.
 */
actual fun openUrl(
    url: String,
    platformContext: Any?,
    onFailure: () -> Unit
) {
    val nsUrl = NSURL.URLWithString(url)
    if (nsUrl != null) {
        UIApplication.sharedApplication.openURL(
            url = nsUrl,
            options = emptyMap<Any?, Any>(),
            completionHandler = null
        )
    }
}