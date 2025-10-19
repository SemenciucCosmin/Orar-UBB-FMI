package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.formatTime
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.custom.OutlinedTextField
import com.ubb.fmi.orar.ui.catalog.components.form.DayMultiselectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.FrequencySelectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.TimePicker
import com.ubb.fmi.orar.ui.catalog.components.timetable.EventCard
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_activity
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_add_personal_event
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_caption
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_location
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_next
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val DETAILS_LINES = 3
private const val DETAILS_MAX_CHARS = 100

@Suppress("LongParameterList", "MagicNumber")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPersonalEventScreen(
    activity: String,
    location: String,
    caption: String,
    details: String,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    selectedFrequency: Frequency,
    selectedDays: List<Day>,
    isNextEnabled: Boolean,
    onActivityChange: (String) -> Unit,
    onCaptionChange: (String) -> Unit,
    onLocationChange: (String) -> Unit,
    onDetailsChange: (String) -> Unit,
    onStartHourChange: (Int) -> Unit,
    onStartMinuteChange: (Int) -> Unit,
    onEndHourChange: (Int) -> Unit,
    onEndMinuteChange: (Int) -> Unit,
    onSelectFrequency: (Frequency) -> Unit,
    onSelectDays: (List<Day>) -> Unit,
    onNextClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_add_personal_event),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(Pds.spacing.Medium)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = activity,
                onValueChange = onActivityChange,
                singleLine = true,
                isMandatory = true,
                label = stringResource(Res.string.lbl_activity),
                placeholder = stringResource(Res.string.lbl_activity),
            )

            Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Medium)) {
                OutlinedTextField(
                    modifier = Modifier.weight(0.5f),
                    value = location,
                    onValueChange = onLocationChange,
                    singleLine = true,
                    label = stringResource(Res.string.lbl_location),
                    placeholder = stringResource(Res.string.lbl_location),
                )

                OutlinedTextField(
                    modifier = Modifier.weight(0.5f),
                    value = caption,
                    onValueChange = onCaptionChange,
                    singleLine = true,
                    label = stringResource(Res.string.lbl_caption),
                    placeholder = stringResource(Res.string.lbl_caption),
                )
            }

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = details,
                onValueChange = { if (it.length < DETAILS_MAX_CHARS) onDetailsChange(it) },
                minLines = DETAILS_LINES,
                maxLines = DETAILS_LINES,
                label = stringResource(Res.string.lbl_details),
                placeholder = stringResource(Res.string.lbl_details),
            )

            Spacer(modifier = Modifier.size(Pds.spacing.Medium))

            DayMultiselectionRow(
                selectedDays = selectedDays,
                onSelectDays = onSelectDays,
            )

            Spacer(modifier = Modifier.size(Pds.spacing.Medium))

            FrequencySelectionRow(
                selectedFrequency = selectedFrequency,
                onFrequencyClick = onSelectFrequency,
            )

            Spacer(modifier = Modifier.size(Pds.spacing.XSmall))

            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimePicker(
                    hour = startHour,
                    minute = startMinute,
                    onHourChanged = onStartHourChange,
                    onMinuteChanged = onStartMinuteChange
                )

                Text(
                    text = String.DASH,
                    style = MaterialTheme.typography.titleMedium
                )

                TimePicker(
                    hour = endHour,
                    minute = endMinute,
                    onHourChanged = onEndHourChange,
                    onMinuteChanged = onEndMinuteChange
                )
            }

            Spacer(modifier = Modifier.size(Pds.spacing.Medium))

            EventCard(
                startTime = formatTime(startHour, startMinute),
                endTime = formatTime(endHour, endMinute),
                location = location.ifEmpty { stringResource(Res.string.lbl_location) },
                title = activity.ifEmpty { stringResource(Res.string.lbl_activity) },
                type = EventType.PERSONAL,
                participant = String.BLANK,
                caption = caption.ifEmpty { stringResource(Res.string.lbl_caption) },
                details = details.ifEmpty { stringResource(Res.string.lbl_details) },
                enabled = true,
                expanded = false,
            )

            Spacer(modifier = Modifier.weight(1f))

            PrimaryButton(
                onClick = onNextClick,
                enabled = isNextEnabled,
                text = stringResource(Res.string.lbl_next),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAddPersonalEventScreen() {
    OrarUbbFmiTheme {
        AddPersonalEventScreen(
            activity = "",
            caption = "",
            location = "",
            details = "",
            startHour = 12,
            startMinute = 0,
            endHour = 12,
            endMinute = 0,
            selectedFrequency = Frequency.BOTH,
            selectedDays = listOf(Day.MONDAY, Day.FRIDAY, Day.SATURDAY),
            isNextEnabled = true,
            onActivityChange = {},
            onCaptionChange = {},
            onLocationChange = {},
            onDetailsChange = {},
            onStartHourChange = {},
            onStartMinuteChange = {},
            onEndHourChange = {},
            onEndMinuteChange = {},
            onSelectFrequency = {},
            onSelectDays = {},
            onNextClick = {},
            onBack = {},
        )
    }
}
