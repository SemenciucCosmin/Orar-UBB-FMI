@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.domain.colorpack.extensions

import com.ubb.fmi.orar.domain.extensions.HASH

private const val HEX_RGB_LENGTH = 6
private const val OPAQUE_ALPHA = 0xFF000000
private const val RGB_MASK = 0xFFFFFFL

/**
 * Formats this ARGB color as an opaque "#RRGGBB" hex string.
 */
fun Long.argbToHex(): String {
    val rgb = this and RGB_MASK
    return String.HASH + rgb.toString(16).uppercase().padStart(HEX_RGB_LENGTH, '0')
}

/**
 * Parses a "RRGGBB" or "#RRGGBB" hex string into an opaque ARGB color, or null when invalid.
 */
fun String.hexToArgbOrNull(): Long? {
    val hex = trim().removePrefix(String.HASH)
    if (hex.length != HEX_RGB_LENGTH) return null
    val rgb = hex.toLongOrNull(16) ?: return null
    return OPAQUE_ALPHA or rgb
}
