package com.ubb.fmi.orar.domain.extensions

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri

/**
 * Opens the given [url] in the system’s default browser.
 * Uses an [Intent]; requires [platformContext] as a [Context] (e.g. `LocalContext.current`).
 * @param url The URL to open (must be absolute).
 * @param platformContext Optional context, required only on Android.
 */
actual fun openUrl(
    url: String,
    platformContext: Any?,
    onFailure: () -> Unit,
) {
    val context = platformContext as? Context ?: return
    val intent = Intent(Intent.ACTION_VIEW, url.toUri()).apply {
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }

    try {
        context.startActivity(intent)
    } catch (_: Exception) {
        onFailure
    }
}