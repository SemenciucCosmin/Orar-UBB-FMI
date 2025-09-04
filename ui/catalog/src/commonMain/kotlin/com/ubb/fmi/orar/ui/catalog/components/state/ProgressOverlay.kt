package com.ubb.fmi.orar.ui.catalog.components.state

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.catalog.extensions.conditional
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val ALPHA = 0.7f

/**
 * A composable that displays a progress indicator overlay.
 *
 * @param modifier Modifier to be applied to the overlay.
 * @param hasBackground Whether the overlay should have a background color.
 */
@Composable
fun ProgressOverlay(
    modifier: Modifier = Modifier,
    hasBackground: Boolean = false
) {
    Box(
        contentAlignment = Alignment.Center,
        content = { CircularProgressIndicator(strokeWidth = Pds.spacing.XXSmall) },
        modifier = modifier.conditional(hasBackground) {
            background(MaterialTheme.colorScheme.background.copy(alpha = ALPHA))
        }
    )
}

@Preview
@Composable
private fun PreviewProgressOverlay() {
    OrarUbbFmiTheme {
        ProgressOverlay()
    }
}
