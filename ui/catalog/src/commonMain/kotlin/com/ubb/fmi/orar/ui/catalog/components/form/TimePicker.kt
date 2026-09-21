package com.ubb.fmi.orar.ui.catalog.components.form

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.DialogProperties
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_cancel
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_ok
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePicker(
    state: TimePickerState,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    BasicAlertDialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(),
        content = {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(IntrinsicSize.Max)
                ) {
                    Spacer(modifier = Modifier.size(Pds.spacing.Medium))
                    TimeInput(state = state)

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(
                            Pds.spacing.Medium,
                            Alignment.End
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Pds.spacing.Medium)
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text(text = stringResource(Res.string.lbl_cancel))
                        }

                        TextButton(onClick = onConfirm) {
                            Text(text = stringResource(Res.string.lbl_ok))
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
private fun PreviewTimePicker() {
    OrarUbbFmiTheme {
        TimePicker(
            state = TimePickerState(
                initialHour = 12,
                initialMinute = 30,
                is24Hour = true
            ),
            onConfirm = {},
            onDismiss = {}
        )
    }
}