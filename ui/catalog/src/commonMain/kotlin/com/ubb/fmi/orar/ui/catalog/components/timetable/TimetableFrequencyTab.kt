package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a tab for selecting the frequency of a timetable.
 * @param selectedFrequency The currently selected frequency.
 * @param onFrequencyClick Callback invoked when a frequency is clicked, passing the selected frequency.
 * @param modifier Modifier to be applied to the row.
 */
@Composable
fun TimetableFrequencyTab(
    selectedFrequency: Frequency,
    onFrequencyClick: (Frequency) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilterChip(
            selected = selectedFrequency == Frequency.WEEK_1,
            onClick = { onFrequencyClick(Frequency.WEEK_1) },
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(
                    text = stringResource(Frequency.WEEK_1.labelRes),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )

        FilterChip(
            selected = selectedFrequency == Frequency.WEEK_2,
            onClick = { onFrequencyClick(Frequency.WEEK_2) },
            shape = MaterialTheme.shapes.medium,
            label = {
                Text(
                    text = stringResource(Frequency.WEEK_2.labelRes),
                    style = MaterialTheme.typography.labelLarge
                )
            }
        )
    }
}

@Preview
@Composable
private fun PreviewTimetableFrequencyTab() {
    OrarUbbFmiTheme {
        TimetableFrequencyTab(
            selectedFrequency = Frequency.WEEK_1,
            onFrequencyClick = {}
        )
    }
}
