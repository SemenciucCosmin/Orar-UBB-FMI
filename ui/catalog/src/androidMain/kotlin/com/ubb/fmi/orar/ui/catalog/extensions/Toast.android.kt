package com.ubb.fmi.orar.ui.catalog.extensions

import android.content.Context
import android.widget.Toast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength

/**
 * Android specific implementation of function to show toast
 */
actual fun showToast(context: Any?, message: String, length: ToastLength) {
    Toast.makeText(
        context as? Context ?: return,
        message,
        when (length) {
            ToastLength.LONG -> Toast.LENGTH_LONG
            ToastLength.SHORT -> Toast.LENGTH_SHORT
        }
    ).show()
}
