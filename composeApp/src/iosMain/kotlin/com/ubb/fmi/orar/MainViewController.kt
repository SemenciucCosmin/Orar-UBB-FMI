package com.ubb.fmi.orar

import androidx.compose.runtime.getValue
import androidx.compose.ui.window.ComposeUIViewController
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ubb.fmi.orar.app.AppGraph
import com.ubb.fmi.orar.di.KoinInitializer
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.domain.theme.usecase.GetThemeOptionUseCase
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.koin.compose.koinInject
import kotlin.getValue

/**
 * Main view controller for the Orar UBB FMI application on iOS.
 * This function initializes Koin and sets up the main content view using Compose.
 */
@Suppress("FunctionNaming")
fun MainViewController() = ComposeUIViewController(
    configure = { KoinInitializer.initKoin() }
) {
    val getThemeOptionUseCase: GetThemeOptionUseCase = koinInject()
    val themeOption by getThemeOptionUseCase().collectAsStateWithLifecycle(
        initialValue = ThemeOption.SYSTEM
    )

    OrarUbbFmiTheme(themeOption) {
        AppGraph()
    }
}
