package com.ubb.fmi.orar.ui.catalog.components.animation

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun AnimatedCard(
    enabled: Boolean,
    animatedCardState: AnimatedCardState,
    onClick: () -> Unit,
    faceContent: @Composable (modifier: Modifier) -> Unit,
    backContent: @Composable (modifier: Modifier) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        enabled = enabled,
        onClick = onClick,
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = animatedCardState.elevation
        ),
        modifier = modifier.graphicsLayer {
            rotationX = animatedCardState.xRotation
            rotationY = animatedCardState.yRotation
            rotationZ = animatedCardState.zRotation
        }
    ) {
        Box {
            faceContent(
                Modifier
                    .align(Alignment.Center)
                    .alpha(animatedCardState.faceVisibilityAlpha)
            )

            backContent(
                Modifier
                    .align(Alignment.Center)
                    .alpha(animatedCardState.backVisibilityAlpha)
            )
        }
    }
}
