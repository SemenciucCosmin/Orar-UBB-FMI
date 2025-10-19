package com.ubb.fmi.orar.ui.catalog.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.ubb.fmi.orar.domain.extensions.formatTime
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_cancel
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_ok
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
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
        BasicAlertDialog(
            onDismissRequest = { isPickerOpened = false },
            properties = DialogProperties(),
            content = {
                Surface(shape = MaterialTheme.shapes.medium) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(IntrinsicSize.Max)
                    ) {
                        Spacer(modifier = Modifier.size(Pds.spacing.Medium))
                        TimeInput(state = startTimePickerState)

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(
                                Pds.spacing.Medium,
                                Alignment.End
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(Pds.spacing.Medium)
                        ) {
                            TextButton(onClick = { isPickerOpened = false }) {
                                Text(text = stringResource(Res.string.lbl_cancel))
                            }

                            TextButton(
                                onClick = {
                                    onHourChanged(startTimePickerState.hour)
                                    onMinuteChanged(startTimePickerState.minute)
                                    isPickerOpened = false
                                }
                            ) {
                                Text(text = stringResource(Res.string.lbl_ok))
                            }
                        }
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun PreviewHourPicker() {
    OrarUbbFmiTheme {
        TimePicker(
            hour = 12,
            minute = 34,
            onHourChanged = {},
            onMinuteChanged = {}
        )
    }
}