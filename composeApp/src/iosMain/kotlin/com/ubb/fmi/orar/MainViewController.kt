package com.ubb.fmi.orar

import androidx.compose.ui.window.ComposeUIViewController
import com.ubb.fmi.orar.di.KoinInitializer

fun MainViewController() = ComposeUIViewController(
    configure = { KoinInitializer.initKoin() }
) { App() }