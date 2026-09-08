package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.ui.catalog.components.animation.AnimatedCard
import com.ubb.fmi.orar.ui.catalog.components.animation.rememberAnimatedCardState
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme

/**
 * Composable for the Event card
 * Uses a [AnimatedCard] with [EventFace] and [EventBack]
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
    onAddClick: (() -> Unit)? = null,
) {
    val animatedCardState = rememberAnimatedCardState()

    AnimatedCard(
        modifier = modifier,
        enabled = enabled,
        animatedCardState = animatedCardState,
        onClick = { animatedCardState.animateShake() },
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
                text = details,
                onAddClick = onAddClick
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
            expanded = true,
            onAddClick = {}
        )
    }
}