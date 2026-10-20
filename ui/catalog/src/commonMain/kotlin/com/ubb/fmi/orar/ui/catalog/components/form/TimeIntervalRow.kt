package com.ubb.fmi.orar.ui.catalog.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_right_arrow
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_end_hour
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_start_hour
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_time_interval_error
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Start and end time pickers of an event, with an error when the end isn't after the start
 */
@Suppress("LongParameterList")
@Composable
fun TimeIntervalRow(
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    isValid: Boolean,
    onStartHourChange: (Int) -> Unit,
    onStartMinuteChange: (Int) -> Unit,
    onEndHourChange: (Int) -> Unit,
    onEndMinuteChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            verticalAlignment = Alignment.Bottom
        ) {
            TimeField(
                label = stringResource(Res.string.lbl_start_hour),
                hour = startHour,
                minute = startMinute,
                onHourChange = onStartHourChange,
                onMinuteChange = onStartMinuteChange,
                modifier = Modifier.weight(1f)
            )

            Icon(
                painter = painterResource(Res.drawable.ic_right_arrow),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(Pds.icon.Small)
                    .align(Alignment.CenterVertically)
            )

            TimeField(
                label = stringResource(Res.string.lbl_end_hour),
                hour = endHour,
                minute = endMinute,
                onHourChange = onEndHourChange,
                onMinuteChange = onEndMinuteChange,
                modifier = Modifier.weight(1f)
            )
        }

        if (!isValid) {
            FormFieldError(text = stringResource(Res.string.lbl_time_interval_error))
        }
    }
}

@Composable
private fun TimeField(
    label: String,
    hour: Int,
    minute: Int,
    onHourChange: (Int) -> Unit,
    onMinuteChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        FormFieldLabel(text = label)

        TimeChip(
            hour = hour,
            minute = minute,
            onHourChanged = onHourChange,
            onMinuteChanged = onMinuteChange
        )
    }
}
