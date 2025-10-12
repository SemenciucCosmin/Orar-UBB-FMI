@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Location
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a timetable list item with details about an event.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableListItem(
    startHour: String,
    endHour: String,
    location: Location?,
    title: String,
    type: EventType,
    participantName: String,
    hostName: String,
    enabled: Boolean,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    val tooltipState = rememberTooltipState()

    TooltipBox(
        modifier = modifier,
        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
        tooltip = { location?.address?.let { PlainTooltip { Text(it) } } },
        state = tooltipState,
    ) {
        ElevatedCard(
            modifier = modifier,
            enabled = enabled,
            onClick = {
                coroutineScope.launch {
                    when {
                        tooltipState.isVisible -> tooltipState.dismiss()
                        else -> tooltipState.show()
                    }
                }
            }
        ) {
            Row(
                modifier = Modifier.padding(Pds.spacing.SMedium),
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
                        text = participantName,
                        type = type,
                        enabled = enabled
                    )

                    Text(
                        text = hostName,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                }

                AnimatedVisibility(expanded) {
                    Spacer(modifier = Modifier.width(Pds.spacing.SMedium))
                }

                EventLocation(
                    text = location?.name ?: String.BLANK,
                    type = type,
                    modifier = Modifier.weight(0.15f),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTimetableListItem() {
    OrarUbbFmiTheme {
        TimetableListItem(
            startHour = "14:00",
            endHour = "16:00",
            title = "Analiza Matematica",
            type = EventType.LABORATORY,
            participantName = "914",
            hostName = "Asist. LORINCZI Abel",
            location = Location(
                id = "A304",
                name = "A304",
                address = "Str. Teodor Mihali nr. 38-40"
            ),
            enabled = true,
            expanded = true
        )
    }
}