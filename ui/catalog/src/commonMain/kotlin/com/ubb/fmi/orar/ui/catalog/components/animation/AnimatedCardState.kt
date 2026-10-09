package com.ubb.fmi.orar.ui.catalog.components.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * Composable that creates and remembers an [AnimatedCardState] with proper lifecycle management.
 * Automatically cancels any running animations when the state is disposed.
 *
 * @param config Optional configuration for animation timings and behavior. Defaults to [AnimatedCardConfig.Default].
 * @return [AnimatedCardState] instance tied to the composition lifecycle.
 */
@Composable
fun rememberAnimatedCardState(
    config: AnimatedCardConfig = AnimatedCardConfig.Default,
): AnimatedCardState {
    val coroutineScope = rememberCoroutineScope()
    val state = remember(config) { AnimatedCardState(coroutineScope, config) }

    DisposableEffect(state) {
        onDispose { state.cancelAnimation() }
    }

    return state
}

/**
 * Configuration data class for customizing [AnimatedCardState] animation behavior.
 * Allows different cards to have different animation speeds, intensities, and durations.
 *
 * @param flipAnimationDurationMs Duration in milliseconds for the flip animation. Default: 700ms
 * @param shakeAnimationDurationMs Duration in milliseconds for individual shake steps. Default: 40ms
 * @param shakeRotationDegrees Rotation angle in degrees for each shake iteration. Default: 2°
 * @param shakeIterations Number of shake back-and-forth cycles. Default: 10
 * @param minElevation Minimum elevation value for the card. Default: 1f
 * @param maxElevation Maximum elevation value during shake animation. Default: 15f
 */
data class AnimatedCardConfig(
    val flipAnimationDurationMs: Int = DEFAULT_FLIP_ANIMATION_TIME,
    val shakeAnimationDurationMs: Int = DEFAULT_SHAKE_ANIMATION_TIME,
    val shakeRotationDegrees: Float = DEFAULT_SHARP_ROTATION,
    val shakeIterations: Int = DEFAULT_SHAKE_ANIMATIONS_COUNT,
    val minElevation: Float = DEFAULT_MIN_ELEVATION,
    val maxElevation: Float = DEFAULT_MAX_ELEVATION,
) {
    companion object {
        val Default = AnimatedCardConfig()

        private const val DEFAULT_FLIP_ANIMATION_TIME = 700
        private const val DEFAULT_SHAKE_ANIMATION_TIME = 40
        private const val DEFAULT_SHARP_ROTATION = 2f
        private const val DEFAULT_SHAKE_ANIMATIONS_COUNT = 15
        private const val DEFAULT_MIN_ELEVATION = 1f
        private const val DEFAULT_MAX_ELEVATION = 15f
    }
}

/**
 * Manages animation state for a flip card component.
 * Handles rotation animations (flip), shake animations, and elevation changes.
 *
 * Features:
 * - Single animation guard: only one animation can run at a time
 * - Configurable animation behavior via [AnimatedCardConfig]
 * - State introspection: query whether face or back is showing
 * - Lifecycle-aware: integrates with Compose DisposableEffect for cleanup
 *
 * @param coroutineScope Coroutine scope for launching animations
 * @param config Animation configuration with customizable timings and values
 */
@Stable
class AnimatedCardState(
    private val coroutineScope: CoroutineScope,
    private val config: AnimatedCardConfig = AnimatedCardConfig.Default,
) {
    private var animationJob: Job? = null

    private val _xRotation = Animatable(NO_ROTATION)
    val xRotation: Float
        get() = _xRotation.value

    private val _yRotation = Animatable(NO_ROTATION)
    val yRotation: Float
        get() = _yRotation.value

    private val _zRotation = Animatable(NO_ROTATION)
    val zRotation: Float
        get() = _zRotation.value

    private var isCardFaceSide by mutableStateOf(true)

    /**
     * Public getter to query whether the card's front face is currently showing.
     */
    val isFaceShowing: Boolean
        get() = isCardFaceSide

    /**
     * Calculates alpha visibility for the front face (1f when visible, 0f when back shows).
     */
    val faceVisibilityAlpha: Float
        get() = if (isCardFaceSide) VISIBLE_ALPHA else INVISIBLE_ALPHA

    /**
     * Calculates alpha visibility for the back face (1f when visible, 0f when front shows).
     */
    val backVisibilityAlpha: Float
        get() = if (isCardFaceSide) INVISIBLE_ALPHA else VISIBLE_ALPHA

    private val _elevation = Animatable(config.minElevation)
    val elevation: Dp
        get() = _elevation.value.dp

    /**
     * Animates the card flip (180° rotation on X axis).
     * Toggles between front and back face visibility.
     * Ignored if animation is already running.
     */
    fun animateFlip() {
        if (animationJob?.isActive == true) return
        animationJob = coroutineScope.launch {
            _xRotation.animateTo(
                animationSpec = tween(config.flipAnimationDurationMs),
                targetValue = when {
                    isCardFaceSide -> FULL_ROTATION
                    else -> NO_ROTATION
                },
                block = {
                    // Swap face visibility at halfway point during animation
                    if (value > HALF_ROTATION && isCardFaceSide) {
                        isCardFaceSide = false
                        coroutineScope.launch {
                            _zRotation.snapTo(FULL_ROTATION)
                            _yRotation.snapTo(FULL_ROTATION)
                        }
                    }

                    if (value < HALF_ROTATION && !isCardFaceSide) {
                        isCardFaceSide = true
                        coroutineScope.launch {
                            _zRotation.snapTo(NO_ROTATION)
                            _yRotation.snapTo(NO_ROTATION)
                        }
                    }
                }
            )
        }
    }

    /**
     * Animates a shake effect: elevation boost → rotation back-and-forth → elevation reset.
     * Sequence:
     * 1. Raise card (elevation)
     * 2. Rotate to initial angle
     * 3. Oscillate left-right N times
     * 4. Return to neutral rotation
     * 5. Lower card (elevation)
     * Ignored if animation is already running.
     */
    fun animateShake() {
        if (animationJob?.isActive == true) return
        animationJob = coroutineScope.launch {
            _elevation.animateTo(
                targetValue = config.maxElevation,
                animationSpec = tween(config.shakeAnimationDurationMs)
            )

            _zRotation.animateTo(
                targetValue = config.shakeRotationDegrees,
                animationSpec = tween(config.shakeAnimationDurationMs / 2)
            )

            repeat(config.shakeIterations) {
                _zRotation.animateTo(
                    animationSpec = tween(config.shakeAnimationDurationMs),
                    targetValue = when {
                        _zRotation.value == config.shakeRotationDegrees -> {
                            -config.shakeRotationDegrees
                        }

                        else -> config.shakeRotationDegrees
                    }
                )
            }

            _zRotation.animateTo(
                targetValue = NO_ROTATION,
                animationSpec = tween(config.shakeAnimationDurationMs / 2)
            )

            _elevation.animateTo(
                targetValue = config.minElevation,
                animationSpec = tween(config.shakeAnimationDurationMs)
            )
        }
    }

    /**
     * Resets all rotation and elevation values to neutral state.
     * Cancels any running animations before resetting.
     */
    fun resetCard() {
        cancelAnimation()
        isCardFaceSide = true
        coroutineScope.launch {
            _xRotation.snapTo(NO_ROTATION)
            _yRotation.snapTo(NO_ROTATION)
            _zRotation.snapTo(NO_ROTATION)
            _elevation.snapTo(config.minElevation)
        }
    }

    /**
     * Cancels any ongoing animation job.
     * Called automatically by [DisposableEffect] when state is disposed.
     */
    fun cancelAnimation() {
        animationJob?.cancel()
        animationJob = null
    }

    companion object {
        private const val NO_ROTATION = 0f
        private const val HALF_ROTATION = 90f
        private const val FULL_ROTATION = 180f
        private const val VISIBLE_ALPHA = 1f
        private const val INVISIBLE_ALPHA = 0f
    }
}
