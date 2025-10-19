package com.ubb.fmi.orar.ui.catalog.components.form

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

 */
@Composable
fun FrequencySelectionRow(
    selectedFrequency: Frequency,
    onFrequencyClick: (Frequency) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Frequency.entries.forEach { frequency ->
            FilterChip(
                selected = selectedFrequency == frequency,
                onClick = { onFrequencyClick(frequency) },
                shape = MaterialTheme.shapes.small,
                label = {
                    Text(
                        text = stringResource(frequency.labelRes),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewFrequencySelectionRow() {
    OrarUbbFmiTheme {
        FrequencySelectionRow(
            selectedFrequency = Frequency.WEEK_1,
            onFrequencyClick = {}
        )
    }
}
