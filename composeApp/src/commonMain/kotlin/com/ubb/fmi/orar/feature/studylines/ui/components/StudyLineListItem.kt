package com.ubb.fmi.orar.feature.studylines.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.ui.catalog.extensions.conditional
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_check
import orar_ubb_fmi.composeapp.generated.resources.ic_right_arrow
import orar_ubb_fmi.composeapp.generated.resources.ic_study_line
import org.jetbrains.compose.resources.painterResource

@Composable
fun StudyLineListItem(
    title: String,
    studyLevels: List<StudyLevel>,
    isSelected: Boolean,
    onClick: () -> Unit,
    onStudyLevelClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier.conditional(isSelected) {
            border(
                width = Pds.stroke.Medium,
                color = MaterialTheme.colorScheme.primary,
                shape = MaterialTheme.shapes.medium
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(Pds.spacing.SMedium),
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.size(Pds.icon.SMedium),
                    painter = painterResource(Res.drawable.ic_study_line),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.weight(1f)
                )

                if (isSelected) {
                    Icon(
                        modifier = Modifier.size(Pds.icon.SMedium),
                        painter = painterResource(Res.drawable.ic_check),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            AnimatedVisibility(visible = isSelected) {
                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
                    HorizontalDivider()

                    studyLevels.forEach { studyLevel ->
                        ElevatedCard(onClick = { onStudyLevelClick(studyLevel.id) }) {
                            Row(
                                modifier = Modifier.padding(Pds.spacing.Small),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = studyLevel.label,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.weight(1f)
                                )

                                Icon(
                                    modifier = Modifier.size(Pds.icon.SMedium),
                                    painter = painterResource(Res.drawable.ic_right_arrow),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
