package com.ubb.fmi.orar.ui.catalog.components.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.ui.catalog.extensions.shortLabelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun DayChip(
    day: Day,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FilterChip(
        modifier = modifier.size(Pds.icon.Large),
        selected = selected,
        onClick = onClick,
        shape = CircleShape,
        label = {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(day.shortLabelRes),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    )
}

@Preview
@Composable
private fun PreviewDayChip() {
    OrarUbbFmiTheme {
        Surface {
            DayChip(
                day = Day.SATURDAY,
                selected = false,
                onClick = {}
            )
        }
    }
}