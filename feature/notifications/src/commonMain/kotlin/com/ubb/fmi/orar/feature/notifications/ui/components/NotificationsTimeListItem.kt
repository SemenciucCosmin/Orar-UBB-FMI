package com.ubb.fmi.orar.feature.notifications.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.ui.catalog.components.form.TimePicker
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_hour_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_minute_abr
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_notification_advance_minutes
import org.jetbrains.compose.resources.stringResource

private const val MINUTES_IN_HOUR = 60

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsTimeListItem(
    advanceMinutes: Int,
    advanceHours: Int,
    onSetMinutes: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var isPickerOpened by remember { mutableStateOf(false) }

    ListItemClickable(
        modifier = modifier,
        enabled = enabled,
        headline = stringResource(Res.string.lbl_notification_advance_minutes),
        onClick = { isPickerOpened = true }
    ) {
        Text(
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            text = stringResource(
                Res.string.lbl_hour_abr,
                advanceHours
            ) + String.SPACE + stringResource(
                Res.string.lbl_minute_abr,
                advanceMinutes
            )
        )
    }

    if (isPickerOpened) {
        val timePickerState = remember(advanceHours, advanceMinutes) {
            TimePickerState(
                initialHour = advanceHours,
                initialMinute = advanceMinutes,
                is24Hour = true,
            )
        }

        TimePicker(
            state = timePickerState,
            onDismiss = { isPickerOpened = false },
            onConfirm = {
                onSetMinutes(timePickerState.hour * MINUTES_IN_HOUR + timePickerState.minute)
                isPickerOpened = false
            },
        )
    }
}