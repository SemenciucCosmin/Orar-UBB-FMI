package com.ubb.fmi.orar

import androidx.compose.ui.window.ComposeUIViewController
import com.ubb.fmi.orar.di.KoinInitializer
import com.ubb.fmi.orar.app.AppGraph
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme

@Suppress("FunctionNaming")
fun MainViewController() = ComposeUIViewController(
    configure = { KoinInitializer.initKoin() }
) {
    OrarUbbFmiTheme {
        AppGraph()
    }
}
