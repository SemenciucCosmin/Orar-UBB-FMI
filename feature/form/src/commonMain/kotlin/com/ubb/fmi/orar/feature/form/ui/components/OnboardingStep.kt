package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ubb.fmi.orar.ui.theme.Pds

private const val ENTER_ANIMATION_MILLIS = 400
private const val ENTER_DELAY_STEP_MILLIS = 120

/**
 * Numbered onboarding step with a title, a short explanation and its choices. Steps slide in one
 * after another, based on their [number].
 */
@Composable
fun OnboardingStep(
    number: Int,
    title: String,
    description: String,
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val visibleState = remember { MutableTransitionState(false).apply { targetState = true } }
    val enterSpec = tween<Float>(
        durationMillis = ENTER_ANIMATION_MILLIS,
        delayMillis = number * ENTER_DELAY_STEP_MILLIS
    )

    AnimatedVisibility(
        visibleState = visibleState,
        modifier = modifier,
        enter = fadeIn(enterSpec) + slideInVertically(
            animationSpec = tween(
                durationMillis = ENTER_ANIMATION_MILLIS,
                delayMillis = number * ENTER_DELAY_STEP_MILLIS
            ),
            initialOffsetY = { it / 2 }
        ),
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OnboardingStepBadge(number = number, isCompleted = isCompleted)

                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )

                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            content()
        }
    }
}
