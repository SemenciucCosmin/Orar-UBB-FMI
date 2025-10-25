package com.ubb.fmi.orar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.domain.theme.usecase.GetThemeOptionUseCase
import org.koin.compose.koinInject

/**
 * Composable that determines whether the app is in DARK or LIGHT theme.
 * Uses the user setting on the theme or the system setting
 */
@Composable
fun isAppInDarkTheme(): Boolean {
    val getThemeOptionUseCase: GetThemeOptionUseCase = koinInject()
    val themeOption by getThemeOptionUseCase().collectAsState(initial = ThemeOption.SYSTEM)

    return when (themeOption) {
        ThemeOption.DARK -> true
        ThemeOption.LIGHT -> false
        ThemeOption.SYSTEM -> isSystemInDarkTheme()
    }
}
