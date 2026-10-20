package com.ubb.fmi.orar.feature.customevent.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.formatTime
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.custom.OutlinedTextField
import com.ubb.fmi.orar.ui.catalog.components.form.DayMultiselectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.FormFieldError
import com.ubb.fmi.orar.ui.catalog.components.form.FormFieldLabel
import com.ubb.fmi.orar.ui.catalog.components.form.FormSection
import com.ubb.fmi.orar.ui.catalog.components.form.FrequencySelectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.TimeIntervalRow
import com.ubb.fmi.orar.ui.catalog.components.timetable.EventCard
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.ic_edit
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_activity
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_add
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_add_custom_event
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_caption
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_characters_counter
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_day_selection_error
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_days
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_event_information
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_location
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_preview
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_schedule
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_weeks
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

private const val DETAILS_LINES = 3
private const val DETAILS_MAX_CHARS = 100

/**
 * Form for adding a custom event to the timetable, with a live preview of the event card on top
 * and the add button pinned to the bottom.
 */
@Suppress("LongParameterList", "LongMethod")
@Composable
fun AddCustomEventScreen(
    activity: String,
    location: String,
    caption: String,
    details: String,
    startHour: Int,
    startMinute: Int,
    endHour: Int,
    endMinute: Int,
    selectedFrequency: Frequency,
    selectedDays: ImmutableList<Day>,
    isTimeIntervalValid: Boolean,
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
                title = stringResource(Res.string.lbl_add_custom_event),
                onBack = onBack
            )
        },
        bottomBar = {
            Surface(tonalElevation = Pds.spacing.XSmall) {
                PrimaryButton(
                    onClick = onNextClick,
                    enabled = isNextEnabled,
                    text = stringResource(Res.string.lbl_add),
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding()
                        .imePadding()
                        .padding(Pds.spacing.Medium)
                )
            }
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Pds.spacing.Medium)
        ) {
            FormFieldLabel(text = stringResource(Res.string.lbl_preview))

            EventCard(
                startTime = formatTime(startHour, startMinute),
                endTime = formatTime(endHour, endMinute),
                location = location.ifEmpty { stringResource(Res.string.lbl_location) },
                title = activity.ifEmpty { stringResource(Res.string.lbl_activity) },
                type = EventType.CUSTOM,
                participant = String.BLANK,
                caption = caption.ifEmpty { stringResource(Res.string.lbl_caption) },
                details = details.ifEmpty { stringResource(Res.string.lbl_details) },
                enabled = true,
                expanded = false,
            )

            FormSection(
                title = stringResource(Res.string.lbl_event_information),
                icon = painterResource(Res.drawable.ic_edit),
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = activity,
                    onValueChange = onActivityChange,
                    singleLine = true,
                    isMandatory = true,
                    label = stringResource(Res.string.lbl_activity),
                )

                Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium)) {
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = location,
                        onValueChange = onLocationChange,
                        singleLine = true,
                        label = stringResource(Res.string.lbl_location),
                    )

                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = caption,
                        onValueChange = onCaptionChange,
                        singleLine = true,
                        label = stringResource(Res.string.lbl_caption),
                    )
                }

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = details,
                    onValueChange = { if (it.length <= DETAILS_MAX_CHARS) onDetailsChange(it) },
                    minLines = DETAILS_LINES,
                    maxLines = DETAILS_LINES,
                    label = stringResource(Res.string.lbl_details),
                    supportingText = stringResource(
                        Res.string.lbl_characters_counter,
                        details.length,
                        DETAILS_MAX_CHARS
                    ),
                )
            }

            FormSection(
                title = stringResource(Res.string.lbl_schedule),
                icon = painterResource(Res.drawable.ic_calendar),
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
                    FormFieldLabel(text = stringResource(Res.string.lbl_days))

                    DayMultiselectionRow(
                        selectedDays = selectedDays,
                        onSelectDays = onSelectDays,
                    )

                    if (selectedDays.isEmpty()) {
                        FormFieldError(text = stringResource(Res.string.lbl_day_selection_error))
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
                    FormFieldLabel(text = stringResource(Res.string.lbl_weeks))

                    FrequencySelectionRow(
                        selectedFrequency = selectedFrequency,
                        onFrequencyClick = onSelectFrequency,
                    )
                }

                TimeIntervalRow(
                    startHour = startHour,
                    startMinute = startMinute,
                    endHour = endHour,
                    endMinute = endMinute,
                    isValid = isTimeIntervalValid,
                    onStartHourChange = onStartHourChange,
                    onStartMinuteChange = onStartMinuteChange,
                    onEndHourChange = onEndHourChange,
                    onEndMinuteChange = onEndMinuteChange,
                )
            }
        }
    }
}

@Suppress("MagicNumber")
@Preview
@Composable
private fun PreviewAddCustomEventScreen() {
    OrarUbbFmiTheme {
        AddCustomEventScreen(
            activity = "",
            caption = "",
            location = "",
            details = "",
            startHour = 12,
            startMinute = 0,
            endHour = 14,
            endMinute = 0,
            selectedFrequency = Frequency.BOTH,
            selectedDays = persistentListOf(Day.MONDAY, Day.FRIDAY, Day.SATURDAY),
            isTimeIntervalValid = true,
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
