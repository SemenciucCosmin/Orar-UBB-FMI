package com.ubb.fmi.orar.feature.freerooms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.form.DayMultiselectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.FrequencySelectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.TimePicker
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_location
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays a list of rooms.
 * @param uiState The state of the rooms UI, containing the list of rooms and loading/error states.
 * @param onRoomClick Callback function to be invoked when a room is clicked, passing the room ID.
 * @param onRetryClick Callback function to be invoked when the user clicks the retry button in case of an error.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FreeRoomsScreen(
    uiState: FreeRoomsUiState,
    onRoomClick: (String) -> Unit,
    onStartHourChange: (Int) -> Unit,
    onStartMinuteChange: (Int) -> Unit,
    onEndHourChange: (Int) -> Unit,
    onEndMinuteChange: (Int) -> Unit,
    onSelectFrequency: (Frequency) -> Unit,
    onSelectDays: (List<Day>) -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        isEmpty = uiState.isEmpty,
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_free_rooms),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            Column(modifier = Modifier.padding(Pds.spacing.Medium)) {
                DayMultiselectionRow(
                    selectedDays = uiState.selectedDays,
                    onSelectDays = onSelectDays,
                )

                Spacer(modifier = Modifier.size(Pds.spacing.Medium))

                FrequencySelectionRow(
                    selectedFrequency = uiState.selectedFrequency,
                    onFrequencyClick = onSelectFrequency,
                )

                Spacer(modifier = Modifier.size(Pds.spacing.XSmall))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TimePicker(
                        hour = uiState.startHour,
                        minute = uiState.startMinute,
                        onHourChanged = onStartHourChange,
                        onMinuteChanged = onStartMinuteChange
                    )

                    Text(
                        text = String.DASH,
                        style = MaterialTheme.typography.titleMedium
                    )

                    TimePicker(
                        hour = uiState.endHour,
                        minute = uiState.endMinute,
                        onHourChanged = onEndHourChange,
                        onMinuteChanged = onEndMinuteChange
                    )
                }
            }

            LazyColumn(
                contentPadding = PaddingValues(Pds.spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            ) {
                items(uiState.rooms) { room ->
                    ListItemClickable(
                        headline = room.name,
                        underLine = room.address,
                        onClick = { onRoomClick(room.id) },
                        leadingIcon = painterResource(Res.drawable.ic_location),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRoomsScreen() {
    OrarUbbFmiTheme {
        FreeRoomsScreen(
            onRoomClick = {},
            onRetryClick = {},
            onStartHourChange = {},
            onStartMinuteChange = {},
            onEndHourChange = {},
            onEndMinuteChange = {},
            onSelectFrequency = {},
            onSelectDays = {},
            onBack = {},
            uiState = FreeRoomsUiState(
                isLoading = false,
                errorStatus = null,
                rooms = List(5) {
                    Owner.Room(
                        id = it.toString(),
                        name = "Room $it",
                        configurationId = "20241",
                        address = "Locations $it"
                    )
                }.toImmutableList()
            )
        )
    }
}
