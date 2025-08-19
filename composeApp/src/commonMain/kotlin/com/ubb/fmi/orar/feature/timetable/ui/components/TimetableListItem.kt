package com.ubb.fmi.orar.feature.timetable.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.data.core.model.ClassType
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_down_arrow
import org.jetbrains.compose.resources.painterResource
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
    modifier: Modifier = Modifier
) {
    ElevatedCard(modifier = modifier) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
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
                    modifier = Modifier.size(24.dp),
                    painter = painterResource(Res.drawable.ic_down_arrow),
                    contentDescription = null
                )

                Text(
                    text = endHour,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                modifier = Modifier.weight(0.70f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = subject,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Surface(
                    color = classType.color,
                    shape = MaterialTheme.shapes.extraLarge,
                ) {
                    Text(
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = Color.White,
                        text = when {
                            participant.isEmpty() -> classType.label
                            else -> "${classType.label} - $participant"
                        },
                        modifier = Modifier.padding(
                            vertical = 3.dp,
                            horizontal = 9.dp
                        )
                    )
                }

                Text(
                    text = teacher,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }

            Column(
                modifier = Modifier.weight(0.15f),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    modifier = Modifier.size(32.dp),
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
        )
    }
}