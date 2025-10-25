@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.ui.catalog.extensions.colorDark
import com.ubb.fmi.orar.ui.catalog.extensions.colorLight
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import com.ubb.fmi.orar.ui.theme.isAppInDarkTheme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a chip for event type with certain color and label
 */
@Composable
fun EventTypeChip(
    text: String,
    type: EventType,
    enabled: Boolean,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.extraLarge,
        color = when {
            isAppInDarkTheme() -> type.colorDark
            else -> type.colorLight
        }.copy(
            alpha = when {
                enabled -> 1f
                else -> 0.5f
            }
        )
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.White,
            text = when {
                text.isEmpty() -> stringResource(type.labelRes)
                else -> "${stringResource(type.labelRes)} - $text"
            },
            modifier = Modifier.padding(
                vertical = Pds.spacing.XSmall,
                horizontal = Pds.spacing.SMedium
            )
        )
    }
}

@Preview
@Composable
private fun PreviewEventTypeChip() {
    OrarUbbFmiTheme {
        Column {
            EventType.entries.forEach {
                EventTypeChip(
                    text = "IE1",
                    type = it,
                    enabled = true,
                )
            }
        }
    }
}