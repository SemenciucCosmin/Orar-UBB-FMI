package com.ubb.fmi.orar.feature.freerooms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState.Companion.isTimeIntervalValid
import com.ubb.fmi.orar.ui.catalog.components.form.DayMultiselectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.FormFieldError
import com.ubb.fmi.orar.ui.catalog.components.form.FormFieldLabel
import com.ubb.fmi.orar.ui.catalog.components.form.FormSection
import com.ubb.fmi.orar.ui.catalog.components.form.FrequencySelectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.TimeIntervalRow
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_day_selection_error
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_days
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_now
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_schedule
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_weeks
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Card with the days, weeks and time interval in which the rooms have to be free
 */
@Suppress("LongParameterList")
@Composable
fun FreeRoomsFilters(
    uiState: FreeRoomsUiState,
    onSelectDays: (List<Day>) -> Unit,
    onSelectFrequency: (Frequency) -> Unit,
    onStartHourChange: (Int) -> Unit,
    onStartMinuteChange: (Int) -> Unit,
    onEndHourChange: (Int) -> Unit,
    onEndMinuteChange: (Int) -> Unit,
    onNowClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    FormSection(
        title = stringResource(Res.string.lbl_schedule),
        icon = painterResource(Res.drawable.ic_calendar),
        modifier = modifier,
        trailingContent = {
            AssistChip(
                onClick = onNowClick,
                label = { Text(text = stringResource(Res.string.lbl_free_rooms_now)) }
            )
        }
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
            FormFieldLabel(text = stringResource(Res.string.lbl_days))

            DayMultiselectionRow(
                selectedDays = uiState.selectedDays,
                onSelectDays = onSelectDays,
            )

            if (uiState.selectedDays.isEmpty()) {
                FormFieldError(text = stringResource(Res.string.lbl_day_selection_error))
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
            FormFieldLabel(text = stringResource(Res.string.lbl_weeks))

            FrequencySelectionRow(
                selectedFrequency = uiState.selectedFrequency,
                onFrequencyClick = onSelectFrequency,
            )
        }

        TimeIntervalRow(
            startHour = uiState.startHour,
            startMinute = uiState.startMinute,
            endHour = uiState.endHour,
            endMinute = uiState.endMinute,
            isValid = uiState.isTimeIntervalValid,
            onStartHourChange = onStartHourChange,
            onStartMinuteChange = onStartMinuteChange,
            onEndHourChange = onEndHourChange,
            onEndMinuteChange = onEndMinuteChange,
        )
    }
}
