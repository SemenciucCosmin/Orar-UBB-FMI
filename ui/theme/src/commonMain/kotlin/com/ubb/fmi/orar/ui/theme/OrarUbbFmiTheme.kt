package com.ubb.fmi.orar.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.ubb.fmi.orar.ui.theme.color.DarkColorScheme
import com.ubb.fmi.orar.ui.theme.color.LightColorScheme

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
