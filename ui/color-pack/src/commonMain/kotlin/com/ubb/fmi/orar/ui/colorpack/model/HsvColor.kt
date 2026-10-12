@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.colorpack.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import kotlin.math.max
import kotlin.math.min

/**
 * Color in the HSV color space, used by the color picker so hue is preserved even when
 * saturation or value reach zero.
 *
 * @param hue In range [0, 360).
 * @param saturation In range [0, 1].
 * @param value In range [0, 1].
 */
@Immutable
data class HsvColor(
    val hue: Float,
    val saturation: Float,
    val value: Float,
) {

    fun toColor(): Color = Color.hsv(
        hue = hue.coerceIn(0f, MAX_HUE),
        saturation = saturation.coerceIn(0f, 1f),
        value = value.coerceIn(0f, 1f),
    )

    companion object {
        const val MAX_HUE = 359.99f

        fun from(color: Color): HsvColor {
            val r = color.red
            val g = color.green
            val b = color.blue
            val maxChannel = max(r, max(g, b))
            val minChannel = min(r, min(g, b))
            val delta = maxChannel - minChannel

            val hue = when {
                delta == 0f -> 0f
                maxChannel == r -> 60f * (((g - b) / delta).mod(6f))
                maxChannel == g -> 60f * (((b - r) / delta) + 2f)
                else -> 60f * (((r - g) / delta) + 4f)
            }
            val saturation = if (maxChannel == 0f) 0f else delta / maxChannel

            return HsvColor(
                hue = hue.coerceIn(0f, MAX_HUE),
                saturation = saturation,
                value = maxChannel,
            )
        }
    }
}
