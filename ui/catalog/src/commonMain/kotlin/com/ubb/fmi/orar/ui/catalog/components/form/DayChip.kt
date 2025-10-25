package com.ubb.fmi.orar.ui.catalog.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconToggleButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
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
    val borderColor = when {
        selected -> Color.Transparent
        else -> MaterialTheme.colorScheme.outlineVariant
    }

    OutlinedIconToggleButton(
        checked = selected,
        onCheckedChange = { onClick() },
        modifier = modifier.size(Pds.icon.Large),
        shape = CircleShape,
        border = BorderStroke(1.dp, borderColor),
        colors = IconButtonDefaults.outlinedIconToggleButtonColors().copy(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            checkedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            checkedContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        content = {
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