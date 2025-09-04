package com.ubb.fmi.orar

import androidx.compose.ui.window.ComposeUIViewController
import com.ubb.fmi.orar.app.AppGraph
import com.ubb.fmi.orar.di.KoinInitializer
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme

/**
 * Main view controller for the Orar UBB FMI application on iOS.
 * This function initializes Koin and sets up the main content view using Compose.
 */
@Suppress("FunctionNaming")
fun MainViewController() = ComposeUIViewController(
    configure = { KoinInitializer.initKoin() }
) {
    OrarUbbFmiTheme {
        AppGraph()
    }
}
