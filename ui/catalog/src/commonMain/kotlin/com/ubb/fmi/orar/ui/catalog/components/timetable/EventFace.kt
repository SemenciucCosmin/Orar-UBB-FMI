package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable for the face part of an Event card
 */
@Suppress("MagicNumber")
@Composable
fun EventFace(
    startHour: String,
    endHour: String,
    location: String,
    title: String,
    type: EventType,
    participant: String,
    caption: String,
    enabled: Boolean,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.padding(Pds.spacing.SMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        EventHourInterval(
            modifier = Modifier.weight(0.15f),
            startHour = startHour,
            endHour = endHour
        )

        AnimatedVisibility(expanded) {
            Spacer(modifier = Modifier.width(Pds.spacing.SMedium))
        }

        Column(
            modifier = Modifier.weight(0.70f),
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
            )

            EventTypeChip(
                text = participant,
                type = type,
                enabled = enabled
            )

            Text(
                text = caption,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        AnimatedVisibility(expanded) {
            Spacer(modifier = Modifier.width(Pds.spacing.SMedium))
        }

        EventLocation(
            text = location,
            type = type,
            modifier = Modifier.weight(0.15f),
        )
    }
}

@Preview
@Composable
private fun PreviewEventFace() {
    OrarUbbFmiTheme {
        EventFace(
            startHour = "14:00",
            endHour = "16:00",
            title = "Analiza Matematica",
            type = EventType.LABORATORY,
            participant = "914",
            caption = "Asist. LORINCZI Abel",
            location = "A304",
            enabled = true,
            expanded = true
        )
    }
}