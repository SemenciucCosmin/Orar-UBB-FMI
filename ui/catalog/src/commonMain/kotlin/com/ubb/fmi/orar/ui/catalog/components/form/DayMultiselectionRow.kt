package com.ubb.fmi.orar.ui.catalog.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DayMultiselectionRow(
    selectedDays: List<Day>,
    onSelectDays: (List<Day>) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.selectableGroup(),
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small)
    ) {
        Day.entries.forEach { day ->
            DayChip(
                day = day,
                selected = day in selectedDays,
                onClick = {
                    when {
                        day in selectedDays -> onSelectDays(selectedDays - day)
                        else -> onSelectDays(selectedDays + day)
                    }
                }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewDayMultiselectionRow() {
    OrarUbbFmiTheme {
        Surface {
            DayMultiselectionRow(
                selectedDays = listOf(Day.MONDAY, Day.FRIDAY, Day.SATURDAY),
                onSelectDays = {},
            )
        }
    }
}
