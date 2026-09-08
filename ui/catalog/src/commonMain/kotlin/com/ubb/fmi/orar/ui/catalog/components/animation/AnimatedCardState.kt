package com.ubb.fmi.orar.ui.catalog.components.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
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

@Composable
fun rememberAnimatedCardState(): AnimatedCardState {
    val coroutineScope = rememberCoroutineScope()
    return remember { AnimatedCardState(coroutineScope) }
}

@Stable
class AnimatedCardState(private val coroutineScope: CoroutineScope) {
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
    val faceVisibilityAlpha: Float
        get() = when {
            isCardFaceSide -> VISIBLE_ALPHA
            else -> INVISIBLE_ALPHA
        }
    val backVisibilityAlpha: Float
        get() = when {
            isCardFaceSide -> INVISIBLE_ALPHA
            else -> VISIBLE_ALPHA
        }

    private val _elevation = Animatable(MIN_ELEVATION)
    val elevation: Dp
        get() = _elevation.value.dp

    fun animateFlip() {
        if (animationJob?.isActive == true) return
        animationJob = coroutineScope.launch {
            _xRotation.animateTo(
                animationSpec = tween(X_ROTATION_ANIMATION_TIME),
                targetValue = when {
                    _xRotation.value == NO_ROTATION -> FULL_ROTATION
                    else -> NO_ROTATION
                },
                block = {
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

    fun animateShake() {
        if (animationJob?.isActive == true) return
        animationJob = coroutineScope.launch {
            _elevation.animateTo(
                animationSpec = tween(ELEVATION_ANIMATION_TIME),
                targetValue = when {
                    _elevation.value == MIN_ELEVATION -> MAX_ELEVATION
                    else -> MIN_ELEVATION
                }
            )

            _zRotation.animateTo(
                animationSpec = tween(Z_ROTATION_ANIMATION_TIME / 2),
                targetValue = SHARP_ROTATION
            )

            repeat(Z_ROTATION_ANIMATIONS_COUNT) {
                _zRotation.animateTo(
                    animationSpec = tween(Z_ROTATION_ANIMATION_TIME),
                    targetValue = when {
                        _zRotation.value == SHARP_ROTATION -> -SHARP_ROTATION
                        else -> SHARP_ROTATION
                    }
                )
            }

            _zRotation.animateTo(
                animationSpec = tween(Z_ROTATION_ANIMATION_TIME / 2),
                targetValue = NO_ROTATION
            )

            _elevation.animateTo(
                animationSpec = tween(ELEVATION_ANIMATION_TIME),
                targetValue = when {
                    _elevation.value == MIN_ELEVATION -> MAX_ELEVATION
                    else -> MIN_ELEVATION
                }
            )
        }
    }

    companion object {
        private const val NO_ROTATION = 0f
        private const val SHARP_ROTATION = 2f
        private const val HALF_ROTATION = 90f
        private const val FULL_ROTATION = 180f

        private const val X_ROTATION_ANIMATION_TIME = 700
        private const val Z_ROTATION_ANIMATION_TIME = 40
        private const val Z_ROTATION_ANIMATIONS_COUNT = 10
        private const val ELEVATION_ANIMATION_TIME = 500

        private const val VISIBLE_ALPHA = 1f
        private const val INVISIBLE_ALPHA = 0f

        private const val MIN_ELEVATION = 1f
        private const val MAX_ELEVATION = 15f
    }
}
