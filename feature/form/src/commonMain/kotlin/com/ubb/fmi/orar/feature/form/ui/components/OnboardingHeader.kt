package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_onboarding_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_welcome
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val HALO_ANIMATION_MILLIS = 1600
private const val HALO_MIN_SCALE = 1f
private const val HALO_MAX_SCALE = 1.25f
private const val HALO_MIN_ALPHA = 0.15f
private const val HALO_MAX_ALPHA = 0.4f

/**
 * App icon surrounded by a breathing halo, with a welcome message. Displayed when the timetable
 * is configured for the first time.
 */
@Composable
fun OnboardingHeader(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "halo")
    val haloScale by transition.animateFloat(
        initialValue = HALO_MIN_SCALE,
        targetValue = HALO_MAX_SCALE,
        animationSpec = infiniteRepeatable(tween(HALO_ANIMATION_MILLIS), RepeatMode.Reverse),
        label = "haloScale"
    )
    val haloAlpha by transition.animateFloat(
        initialValue = HALO_MAX_ALPHA,
        targetValue = HALO_MIN_ALPHA,
        animationSpec = infiniteRepeatable(tween(HALO_ANIMATION_MILLIS), RepeatMode.Reverse),
        label = "haloAlpha"
    )

    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = Pds.spacing.Large)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.padding(Pds.spacing.Small)
        ) {
            Box(
                modifier = Modifier
                    .size(Pds.icon.XXLarge)
                    .graphicsLayer {
                        scaleX = haloScale
                        scaleY = haloScale
                        alpha = haloAlpha
                    }
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(Pds.icon.XXLarge)
                    .background(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_calendar),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(Pds.icon.Large)
                )
            }
        }

        Text(
            text = stringResource(Res.string.lbl_welcome),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(Res.string.lbl_onboarding_message),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
