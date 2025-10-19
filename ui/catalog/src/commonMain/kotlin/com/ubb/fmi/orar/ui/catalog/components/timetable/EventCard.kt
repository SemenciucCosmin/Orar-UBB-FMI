package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable for the Event card
 * Uses a [FlippingCard] with [EventFace] and [EventBack]
 */
@Composable
fun EventCard(
    startTime: String,
    endTime: String,
    location: String,
    title: String,
    type: EventType,
    participant: String,
    caption: String,
    details: String,
    enabled: Boolean,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    FlippingCard(
        modifier = modifier,
        enabled = enabled,
        faceContent = { modifier ->
            EventFace(
                modifier = modifier,
                startHour = startTime,
                endHour = endTime,
                location = location,
                title = title,
                type = type,
                participant = participant,
                caption = caption,
                enabled = enabled,
                expanded = expanded,
            )
        },
        backContent = { modifier ->
            EventBack(
                modifier = modifier,
                text = details
            )
        }
    )
}

@Preview
@Composable
private fun PreviewEventCard() {
    OrarUbbFmiTheme {
        EventCard(
            startTime = "14:00",
            endTime = "16:00",
            title = "Analiza Matematica",
            type = EventType.LABORATORY,
            participant = "914",
            caption = "Asist. LORINCZI Abel",
            details = "Str. Teodor Mihali nr. 38-40",
            location = "A304",
            enabled = true,
            expanded = true
        )
    }
}