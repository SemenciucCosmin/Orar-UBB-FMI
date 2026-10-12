package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.stringResource

/**
 * A composable that displays a tab for selecting the frequency of a timetable.
 * @param selectedFrequency The currently selected frequency.
 * @param onFrequencyClick Callback invoked when a frequency is clicked, passing the selected frequency.
 * @param modifier Modifier to be applied to the row.
 * @param currentFrequency The week type of the current calendar week, whose label is followed
 * by a [StatusIndicator]; no week is marked when null.
 */
@Composable
fun TimetableFrequencyTab(
    selectedFrequency: Frequency,
    onFrequencyClick: (Frequency) -> Unit,
    modifier: Modifier = Modifier,
    currentFrequency: Frequency? = null,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        listOf(Frequency.WEEK_1, Frequency.WEEK_2).forEach { frequency ->
            FrequencyChip(
                frequency = frequency,
                selected = selectedFrequency == frequency,
                showIndicator = currentFrequency == frequency,
                onClick = { onFrequencyClick(frequency) },
            )
        }
    }
}

/**
 * A filter chip for a single [frequency], followed by a [StatusIndicator] when [showIndicator].
 */
@Composable
private fun FrequencyChip(
    frequency: Frequency,
    selected: Boolean,
    showIndicator: Boolean,
    onClick: () -> Unit,
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        label = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XXSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.labelLarge,
                    text = stringResource(frequency.labelRes)
                )

                if (showIndicator) {
                    StatusIndicator(defaults = StatusIndicator.Ongoing())
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewTimetableFrequencyTab() {
    OrarUbbFmiTheme {
        TimetableFrequencyTab(
            selectedFrequency = Frequency.WEEK_1,
            onFrequencyClick = {},
            currentFrequency = Frequency.WEEK_2,
        )
    }
}
