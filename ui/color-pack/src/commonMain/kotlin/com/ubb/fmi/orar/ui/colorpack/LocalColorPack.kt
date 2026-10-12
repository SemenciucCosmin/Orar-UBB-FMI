package com.ubb.fmi.orar.ui.colorpack

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack

/**
 * The [ColorPack] available to the composition, defaulting to [ColorPack.Default].
 */
val LocalColorPack = staticCompositionLocalOf { ColorPack.Default }

/**
 * Provides the given [colorPack] to [content] through [LocalColorPack].
 */
@Composable
fun ColorPackProvider(
    colorPack: ColorPack,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(LocalColorPack provides colorPack, content = content)
}
