package com.ubb.fmi.orar.ui.colorpack.extensions

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.toArgb
import com.ubb.fmi.orar.domain.colorpack.extensions.argbToHex
import com.ubb.fmi.orar.domain.colorpack.extensions.hexToArgbOrNull
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack

private const val LUMINANCE_THRESHOLD = 0.5f
private const val ARGB_MASK = 0xFFFFFFFFL

/**
 * Converts the color to its ARGB [Long] representation used by the domain layer.
 */
fun Color.toArgbLong(): Long = toArgb().toLong() and ARGB_MASK

/**
 * Formats the color as an opaque "#RRGGBB" hex string.
 */
fun Color.toHex(): String = toArgbLong().argbToHex()

/**
 * Parses a "RRGGBB" or "#RRGGBB" hex string into an opaque [Color], or null when invalid.
 */
fun String.toColorOrNull(): Color? = hexToArgbOrNull()?.let(::Color)

/**
 * Readable content color (black or white) to be drawn on top of this color.
 */
fun Color.contentColor(): Color = when {
    luminance() > LUMINANCE_THRESHOLD -> Color.Black
    else -> Color.White
}

/**
 * The [Color] to display for [item] in this [ColorPack].
 */
fun ColorPack.getColor(item: ColorItem): Color = Color(this[item])

/**
 * Returns a copy of this pack with [item] set to [color].
 */
fun ColorPack.with(item: ColorItem, color: Color): ColorPack = with(item, color.toArgbLong())
