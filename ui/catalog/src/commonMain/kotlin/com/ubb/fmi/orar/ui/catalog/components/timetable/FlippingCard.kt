package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val NO_ROTATION = 0f
private const val HALF_ROTATION = 90f
private const val FULL_ROTATION = 180f
private const val ANIMATION_TIME = 700
private const val VISIBLE_ALPHA = 1f
private const val INVISIBLE_ALPHA = 0f

/**
 * Composable for a special [ElevatedCard] that can flip on X axis upon clicking.
 * It can receive two composables: one for the face of the card and one for the back.
 */
@Composable
fun FlippingCard(
    enabled: Boolean,
    faceContent: @Composable (modifier: Modifier) -> Unit,
    backContent: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val xRotation = remember { Animatable(NO_ROTATION) }
    var yRotation by remember { mutableStateOf(NO_ROTATION) }
    var zRotation by remember { mutableStateOf(NO_ROTATION) }
    var isFaceSide by remember { mutableStateOf(true) }
    var animationJob by remember { mutableStateOf<Job?>(null) }

    ElevatedCard(
        enabled = enabled,
        modifier = modifier.graphicsLayer {
            rotationX = xRotation.value
            rotationY = yRotation
            rotationZ = zRotation
        },
        onClick = {
            if (animationJob?.isActive == true) return@ElevatedCard
            animationJob = coroutineScope.launch {
                xRotation.animateTo(
                    animationSpec = tween(ANIMATION_TIME),
                    targetValue = when {
                        xRotation.value == NO_ROTATION -> FULL_ROTATION
                        else -> NO_ROTATION
                    },
                    block = {
                        if (value > HALF_ROTATION && isFaceSide) {
                            isFaceSide = false
                            zRotation = FULL_ROTATION
                            yRotation = FULL_ROTATION
                        }

                        if (value < HALF_ROTATION && !isFaceSide) {
                            isFaceSide = true
                            zRotation = NO_ROTATION
                            yRotation = NO_ROTATION
                        }
                    }
                )
            }
        }
    ) {
        Box {
            faceContent(
                Modifier.align(Alignment.Center).alpha(
                    when {
                        isFaceSide -> VISIBLE_ALPHA
                        else -> INVISIBLE_ALPHA
                    }
                )
            )

            backContent(
                Modifier.align(Alignment.Center).alpha(
                    when {
                        isFaceSide -> INVISIBLE_ALPHA
                        else -> VISIBLE_ALPHA
                    }
                )
            )
        }
    }
}