package com.ubb.fmi.orar

import androidx.compose.ui.window.ComposeUIViewController
import com.ubb.fmi.orar.di.KoinInitializer
import com.ubb.fmi.orar.feature.app.ui.navigation.AppGraph

fun MainViewController() = ComposeUIViewController(
    configure = { KoinInitializer.initKoin() }
) { AppGraph() }