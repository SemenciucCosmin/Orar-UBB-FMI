package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_check
import org.jetbrains.compose.resources.painterResource

/**
 * Circle with the [number] of an onboarding step, turning into a check mark once it is completed
 */
@Composable
fun OnboardingStepBadge(
    number: Int,
    isCompleted: Boolean,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val containerColor by animateColorAsState(
        targetValue = if (isCompleted) colorScheme.primary else colorScheme.secondaryContainer,
        label = "stepBadgeContainer"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isCompleted) colorScheme.onPrimary else colorScheme.onSecondaryContainer,
        label = "stepBadgeContent"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(Pds.icon.Medium)
            .background(color = containerColor, shape = CircleShape)
    ) {
        AnimatedContent(
            targetState = isCompleted,
            transitionSpec = { scaleIn() togetherWith scaleOut() },
            label = "stepBadgeIcon"
        ) { completed ->
            when {
                completed -> Icon(
                    painter = painterResource(Res.drawable.ic_check),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(Pds.icon.XSmall)
                )

                else -> Text(
                    text = number.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                )
            }
        }
    }
}
