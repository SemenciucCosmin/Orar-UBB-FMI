package com.ubb.fmi.orar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ubb.fmi.orar.ui.theme.color.DarkColorScheme
import com.ubb.fmi.orar.ui.theme.color.LightColorScheme

/**
 * Represents the theme for the Orar UBB FMI application.
 *
 * This theme applies the appropriate color scheme based on the system's dark mode setting.
 *
 * @param darkTheme Indicates whether the dark theme should be applied. Defaults to the system's dark mode setting.
 * @param content The composable content to be themed.
 */
@Composable
fun OrarUbbFmiTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
