@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.colorpack.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.ui.colorpack.model.HsvColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

private val HUE_COLORS = listOf(0f, 60f, 120f, 180f, 240f, 300f, HsvColor.MAX_HUE).map {
    Color.hsv(hue = it, saturation = 1f, value = 1f)
}

/**
 * A horizontal rainbow slider for picking the [hue] of a color.
 *
 * @param hue The currently selected hue, in range [0, 360).
 * @param onHueChange Callback with the new hue.
 * @param modifier Modifier to be applied to the slider.
 */
@Composable
fun HueSlider(
    hue: Float,
    onHueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnHueChange by rememberUpdatedState(onHueChange)
    val thumbRadius = Pds.spacing.SMedium
    val thumbStroke = Pds.spacing.XXSmall

    fun select(x: Float, width: Float, padding: Float) {
        val trackWidth = width - 2 * padding
        if (trackWidth <= 0f) return
        currentOnHueChange(((x - padding) / trackWidth).coerceIn(0f, 1f) * HsvColor.MAX_HUE)
    }

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(thumbRadius * 2)
            .pointerInput(Unit) {
                val padding = thumbRadius.toPx()
                detectTapGestures { select(it.x, size.width.toFloat(), padding) }
            }
            .pointerInput(Unit) {
                val padding = thumbRadius.toPx()
                detectDragGestures(
                    onDragStart = { select(it.x, size.width.toFloat(), padding) },
                    onDrag = { change, _ -> select(change.position.x, size.width.toFloat(), padding) },
                )
            }
    ) {
        val padding = thumbRadius.toPx()
        val trackHeight = size.height / 2
        val trackWidth = size.width - 2 * padding

        drawRoundRect(
            brush = Brush.horizontalGradient(HUE_COLORS, startX = padding, endX = padding + trackWidth),
            topLeft = Offset(padding, (size.height - trackHeight) / 2),
            size = Size(trackWidth, trackHeight),
            cornerRadius = CornerRadius(trackHeight / 2),
        )

        val center = Offset(padding + trackWidth * hue / HsvColor.MAX_HUE, size.height / 2)
        drawCircle(
            color = Color.hsv(hue = hue, saturation = 1f, value = 1f),
            radius = thumbRadius.toPx(),
            center = center,
        )
        drawCircle(
            color = Color.White,
            radius = thumbRadius.toPx(),
            center = center,
            style = Stroke(width = thumbStroke.toPx()),
        )
    }
}

@Preview
@Composable
private fun PreviewHueSlider() {
    OrarUbbFmiTheme {
        HueSlider(hue = 200f, onHueChange = {})
    }
}
