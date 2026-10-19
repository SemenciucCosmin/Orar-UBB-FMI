package com.ubb.fmi.orar.ui.catalog.extensions

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathOperation
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.addOutline
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.sqrt
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

/**
 * A utility function to conditionally apply a modifier based on a boolean condition.
 *
 * @param condition The condition to check.
 * @param modifier The modifier to apply if the condition is true.
 * @return The original modifier if the condition is false, or the modified one if true.
 */
@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return when {
        condition -> then(modifier(Modifier))
        else -> this
    }
}

/**
 * Draws a border of [width] around [shape], filled with a gradient of [color] that continuously
 * travels around the shape.
 *
 * @param color Color the gradient is built from.
 * @param width Thickness of the border.
 * @param shape Shape the border follows.
 * @param rotationDuration Time for the gradient to complete one full turn.
 */
@Composable
fun Modifier.animatedGradientBorder(
    color: Color,
    width: Dp,
    shape: Shape,
    rotationDuration: Duration = DEFAULT_BORDER_ROTATION_DURATION,
): Modifier {
    val transition = rememberInfiniteTransition(label = "animatedGradientBorder")
    val rotation = transition.animateFloat(
        initialValue = START_ANGLE,
        targetValue = FULL_ANGLE,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = rotationDuration.inWholeMilliseconds.toInt(),
                easing = LinearEasing,
            ),
        ),
        label = "animatedGradientBorderRotation",
    )

    return drawWithCache {
        val strokeWidth = width.toPx()
        val outerPath = Path().apply {
            addOutline(shape.createOutline(size, layoutDirection, this@drawWithCache))
        }
        val innerSize = Size(
            width = (size.width - 2 * strokeWidth).coerceAtLeast(0f),
            height = (size.height - 2 * strokeWidth).coerceAtLeast(0f),
        )
        val innerPath = Path().apply {
            addOutline(shape.createOutline(innerSize, layoutDirection, this@drawWithCache))
            translate(Offset(strokeWidth, strokeWidth))
        }
        val borderPath = Path().apply {
            op(outerPath, innerPath, PathOperation.Difference)
        }
        val brush = Brush.sweepGradient(
            colors = listOf(
                color.copy(alpha = BORDER_FADED_ALPHA),
                color,
                color.copy(alpha = BORDER_FADED_ALPHA),
            ),
            center = size.center,
        )
        val radius = sqrt(size.width * size.width + size.height * size.height) / 2

        onDrawWithContent {
            drawContent()
            clipPath(borderPath) {
                rotate(degrees = rotation.value) {
                    drawCircle(brush = brush, radius = radius, center = center)
                }
            }
        }
    }
}

/**
 * Creates a brush for skeleton placeholders, with a highlight that continuously sweeps across it.
 * Sharing one brush between placeholders keeps them on a single animation, sweeping in sync.
 *
 * @param sweepDuration Time for the highlight to cross the sweep width once.
 */
@Composable
fun rememberShimmerBrush(sweepDuration: Duration = DEFAULT_SHIMMER_SWEEP_DURATION): Brush {
    val color = MaterialTheme.colorScheme.onSurfaceVariant
    val sweepWidth = with(LocalDensity.current) { SHIMMER_SWEEP_WIDTH.toPx() }
    val highlightWidth = with(LocalDensity.current) { SHIMMER_HIGHLIGHT_WIDTH.toPx() }

    val transition = rememberInfiniteTransition(label = "shimmer")
    val offset by transition.animateFloat(
        initialValue = -highlightWidth,
        targetValue = sweepWidth,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = sweepDuration.inWholeMilliseconds.toInt(),
                easing = LinearEasing,
            ),
        ),
        label = "shimmerOffset",
    )

    return Brush.linearGradient(
        colors = listOf(
            color.copy(alpha = SHIMMER_BASE_ALPHA),
            color.copy(alpha = SHIMMER_HIGHLIGHT_ALPHA),
            color.copy(alpha = SHIMMER_BASE_ALPHA),
        ),
        start = Offset(offset, 0f),
        end = Offset(offset + highlightWidth, 0f),
    )
}

private const val START_ANGLE = 0f
private const val FULL_ANGLE = 360f
private const val BORDER_FADED_ALPHA = 0.15f
private val DEFAULT_BORDER_ROTATION_DURATION = 3.seconds
private const val SHIMMER_BASE_ALPHA = 0.12f
private const val SHIMMER_HIGHLIGHT_ALPHA = 0.28f
private val SHIMMER_SWEEP_WIDTH = 600.dp
private val SHIMMER_HIGHLIGHT_WIDTH = 200.dp
private val DEFAULT_SHIMMER_SWEEP_DURATION = 1.2.seconds
