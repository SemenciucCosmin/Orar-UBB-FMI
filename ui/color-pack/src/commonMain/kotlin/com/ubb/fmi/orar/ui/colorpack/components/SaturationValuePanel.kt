package com.ubb.fmi.orar.ui.colorpack.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.colorpack.model.HsvColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

/**
 * A 2D panel for picking saturation (horizontal axis) and value (vertical axis) of [hsvColor].
 *
 * @param hsvColor The currently selected color.
 * @param onChange Callback with the new saturation and value.
 * @param modifier Modifier to be applied to the panel.
 */
@Composable
fun SaturationValuePanel(
    hsvColor: HsvColor,
    onChange: (saturation: Float, value: Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnChange by rememberUpdatedState(onChange)

    fun select(position: Offset, size: IntSize) {
        if (size.width == 0 || size.height == 0) return
        currentOnChange(
            (position.x / size.width).coerceIn(0f, 1f),
            1f - (position.y / size.height).coerceIn(0f, 1f),
        )
    }

    val pureHue = HsvColor(hue = hsvColor.hue, saturation = 1f, value = 1f).toColor()
    val thumbRadius = Pds.spacing.SMedium
    val thumbStroke = Pds.spacing.XXSmall

    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(PANEL_HEIGHT)
            .clip(MaterialTheme.shapes.large)
            .pointerInput(Unit) {
                detectTapGestures { select(it, size) }
            }
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = { select(it, size) },
                    onDrag = { change, _ -> select(change.position, size) },
                )
            }
    ) {
        drawRect(brush = Brush.horizontalGradient(listOf(Color.White, pureHue)))
        drawRect(brush = Brush.verticalGradient(listOf(Color.Transparent, Color.Black)))

        val center = Offset(
            x = hsvColor.saturation * size.width,
            y = (1f - hsvColor.value) * size.height,
        )
        drawCircle(color = hsvColor.toColor(), radius = thumbRadius.toPx(), center = center)
        drawCircle(
            color = Color.White,
            radius = thumbRadius.toPx(),
            center = center,
            style = Stroke(width = thumbStroke.toPx()),
        )
    }
}

private val PANEL_HEIGHT = 220.dp

@Preview
@Composable
private fun PreviewSaturationValuePanel() {
    OrarUbbFmiTheme {
        SaturationValuePanel(
            hsvColor = HsvColor(hue = 140f, saturation = 0.8f, value = 0.7f),
            onChange = { _, _ -> },
        )
    }
}
