package com.ubb.fmi.orar.ui.catalog.extensions

import com.ubb.fmi.orar.ui.catalog.model.ToastLength

/**
 * Expect function to show platform specific toast
 */
expect fun showToast(context: Any?, message: String, length: ToastLength)