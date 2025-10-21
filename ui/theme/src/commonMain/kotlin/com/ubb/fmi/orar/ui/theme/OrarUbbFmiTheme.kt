package com.ubb.fmi.orar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.ui.theme.color.DarkColorScheme
import com.ubb.fmi.orar.ui.theme.color.LightColorScheme

/**
 * Represents the theme for the Orar UBB FMI application.
 * This theme applies the appropriate color scheme based on the system's dark mode setting.
 * @param content The composable content to be themed.
 */
@Composable
fun OrarUbbFmiTheme(content: @Composable () -> Unit) {
//    val koin = getKoin()
//    val getThemeOption: GetThemeOption = koin.get()
//    val themeOption by getThemeOption().collectAsStateWithLifecycle(ThemeOption.SYSTEM)
    val themeOption = ThemeOption.LIGHT

    val colorScheme = when {
        themeOption == ThemeOption.LIGHT -> LightColorScheme
        themeOption == ThemeOption.DARK -> DarkColorScheme
        isSystemInDarkTheme() -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
