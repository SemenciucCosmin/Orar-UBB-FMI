@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.animation.AnimatedVisibility
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
import com.ubb.fmi.orar.domain.timetable.model.ClassType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_down_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimetableListItem(
    startHour: String,
    endHour: String,
    subject: String,
    classType: ClassType,
    participant: String,
    teacher: String,
    room: String,
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
                    text = subject,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Surface(
                    shape = MaterialTheme.shapes.extraLarge,
                    color = classType.color.copy(
                        alpha = when {
                            enabled -> 1f
                            else -> 0.5f
                        }
                    ),
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = when {
                            participant.isEmpty() -> {
                                stringResource(classType.labelRes)
                            }

                            else -> {
                                "${stringResource(classType.labelRes)} - $participant"
                            }
                        },
                        modifier = Modifier.padding(
                            vertical = Pds.spacing.XSmall,
                            horizontal = Pds.spacing.SMedium
                        )
                    )
                }

                Text(
                    text = teacher,
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
                    painter = painterResource(classType.imageRes),
                    contentDescription = null
                )

                Text(
                    text = room,
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
            subject = "Analiza Matematica",
            classType = ClassType.LABORATORY,
            participant = "914",
            teacher = "Asist. LORINCZI Abel",
            room = "A304",
            enabled = true,
            expanded = true
        )
    }
}