package com.ubb.fmi.orar.ui.catalog.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.domain.extensions.formatTime
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeChip(
    hour: Int,
    minute: Int,
    onHourChanged: (Int) -> Unit,
    onMinuteChanged: (Int) -> Unit,
) {
    var isPickerOpened by remember { mutableStateOf(false) }
    val startTimePickerState = rememberTimePickerState(
        initialHour = hour,
        initialMinute = minute,
        is24Hour = true,
    )

    InputChip(
        selected = true,
        onClick = { isPickerOpened = true },
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
        label = {
            Text(
                text = formatTime(hour, minute),
                style = MaterialTheme.typography.titleMedium
            )
        }
    )

    if (isPickerOpened) {
        TimePicker(
            state = startTimePickerState,
            onDismiss = { isPickerOpened = false },
            onConfirm = {
                onHourChanged(startTimePickerState.hour)
                onMinuteChanged(startTimePickerState.minute)
                isPickerOpened = false
            },
        )
    }
}

@Preview
@Composable
private fun PreviewHourChip() {
    OrarUbbFmiTheme {
        TimeChip(
            hour = 12,
            minute = 34,
            onHourChanged = {},
            onMinuteChanged = {}
        )
    }
}