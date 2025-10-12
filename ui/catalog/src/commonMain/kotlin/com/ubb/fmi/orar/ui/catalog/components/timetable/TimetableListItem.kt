@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Location
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.ui.catalog.extensions.colorLight
import com.ubb.fmi.orar.ui.catalog.extensions.imageRes
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_down_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a timetable list item with details about a class.
 *
 * @param startHour The starting hour of the class.
 * @param endHour The ending hour of the class.
 * @param subject The name of the subject.
 * @param classType The type of class (e.g., lecture, laboratory).
 * @param participant The participant or group associated with the class.
 * @param teacher The name of the teacher.
 * @param room The room where the class takes place
 * @param enabled Indicates whether the item is enabled (clickable).
 * @param expanded Indicates whether the item is expanded (showing additional details).
 * @param modifier Modifier to be applied to the card.
 */
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
    ElevatedCard(
        onClick = {},
        modifier = modifier,
        enabled = enabled,
    ) {
        Row(
            modifier = Modifier.padding(Pds.spacing.SMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                Modifier.weight(0.15f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = startHour,
                    style = MaterialTheme.typography.bodyMedium
                )

                Icon(
                    modifier = Modifier.size(Pds.icon.SMedium),
                    painter = painterResource(Res.drawable.ic_down_arrow),
                    contentDescription = null
                )

                Text(
                    text = endHour,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

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

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = when {
                        isSystemInDarkTheme() -> type.colorLight
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
                            participantName.isEmpty() -> {
                                stringResource(type.labelRes)
                            }

                            else -> {
                                "${stringResource(type.labelRes)} - $participantName"
                            }
                        },
                        modifier = Modifier.padding(
                            vertical = Pds.spacing.XSmall,
                            horizontal = Pds.spacing.SMedium
                        )
                    )
                }

                Text(
                    text = hostName,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(expanded) {
                Spacer(modifier = Modifier.width(Pds.spacing.SMedium))
            }

            Column(
                modifier = Modifier.weight(0.15f),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(Pds.icon.Medium),
                    painter = painterResource(type.imageRes),
                    contentDescription = null
                )

                Text(
                    text = location?.name ?: String.BLANK,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodySmall
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