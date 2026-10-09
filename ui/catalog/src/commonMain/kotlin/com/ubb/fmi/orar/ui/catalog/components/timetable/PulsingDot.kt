@file:Suppress("MatchingDeclarationName", "MagicNumber")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme

/**
 * Visual configurations for [PulsingDot].
 */
sealed class PulsingDotDefaults {

    /**
     * The base color from which the dot and ripple colors are derived.
     */
    abstract val color: Color

    /**
     * The color of the solid center dot.
     */
    fun dotColor(): Color = color

    /**
     * The color of a ripple at the given [progress] of its expansion, fading out as it grows.
     *
     * @param progress The ripple's expansion progress, from 0 (just emitted) to 1 (fully expanded).
     */
    fun rippleColor(progress: Float): Color {
        return color.copy(alpha = RIPPLE_START_ALPHA * (1f - progress))
    }

    /**
     * Configuration marking something currently active, such as the current day.
     */
    data class Active(
        override val color: Color = Color(0xFF1DB954),
    ) : PulsingDotDefaults()

    companion object {

        /**
         * The default diameter of the solid center dot.
         */
        val SIZE: Dp = 8.dp

        /**
         * The default ratio between the fully expanded ripple radius and the dot diameter.
         */
        const val RIPPLE_RADIUS_TO_SIZE_RATIO: Float = 1.5f

        /**
         * The default number of ripples visible at the same time.
         */
        const val RIPPLE_COUNT: Int = 2

        /**
         * The default time it takes a single ripple to fully expand and fade out.
         */
        const val RIPPLE_DURATION_MILLIS: Int = 2000

        /**
         * The alpha of a ripple when it is emitted, before fading out.
         */
        const val RIPPLE_START_ALPHA: Float = 0.5f
    }
}

/**
 * A dot with ripple circles continuously spreading out from it and fading away.
 * The ripples are drawn beyond the dot's bounds so they don't affect the layout.
 *
 * @param defaults The visual configuration of the dot.
 * @param modifier Modifier to be applied to the dot.
 * @param size The diameter of the solid center dot.
 * @param rippleRadius The radius a ripple reaches when fully expanded.
 * @param rippleCount The number of ripples visible at the same time, evenly spaced in time.
 * @param rippleDurationMillis The time it takes a single ripple to fully expand and fade out.
 */
@Composable
fun PulsingDot(
    defaults: PulsingDotDefaults,
    modifier: Modifier = Modifier,
    size: Dp = PulsingDotDefaults.SIZE,
    rippleRadius: Dp = size * PulsingDotDefaults.RIPPLE_RADIUS_TO_SIZE_RATIO,
    rippleCount: Int = PulsingDotDefaults.RIPPLE_COUNT,
    rippleDurationMillis: Int = PulsingDotDefaults.RIPPLE_DURATION_MILLIS,
) {
    val transition = rememberInfiniteTransition(label = "pulsingDot")
    val progress = transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = rippleDurationMillis, easing = LinearEasing),
        ),
        label = "pulsingDotProgress",
    )

    Canvas(modifier = modifier.size(size)) {
        val dotRadius = this.size.minDimension / 2
        val maxRippleRadius = rippleRadius.toPx()

        repeat(rippleCount) { index ->
            val rippleProgress = (progress.value + index.toFloat() / rippleCount) % 1f
            drawCircle(
                color = defaults.rippleColor(rippleProgress),
                radius = dotRadius + (maxRippleRadius - dotRadius) * rippleProgress,
            )
        }

        drawCircle(color = defaults.dotColor(), radius = dotRadius)
    }
}

@Preview
@Composable
private fun PreviewPulsingDot() {
    OrarUbbFmiTheme {
        PulsingDot(defaults = PulsingDotDefaults.Active())
    }
}

@Preview
@Composable
private fun PreviewLargePulsingDot() {
    OrarUbbFmiTheme {
        PulsingDot(
            defaults = PulsingDotDefaults.Active(),
            size = 16.dp,
            rippleCount = 3,
        )
    }
}
