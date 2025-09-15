package com.ubb.fmi.orar.ui.catalog.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Composable function for getting the Context from android platform
 */
@Composable
actual fun getContext(): Any? {
    return LocalContext.current
}