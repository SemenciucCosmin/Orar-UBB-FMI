package com.ubb.fmi.orar.feature.freerooms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState.Companion.filteredRooms
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.form.DayMultiselectionRow
import com.ubb.fmi.orar.ui.catalog.components.form.TimePicker
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays a list of rooms.
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
    onSelectDays: (List<Day>) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_free_rooms),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            HorizontalDivider()
            Surface {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Pds.spacing.Medium)
                ) {
                    DayMultiselectionRow(
                        selectedDays = uiState.selectedDays,
                        onSelectDays = onSelectDays,
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
            }

            FreeRoomsList(
                rooms = uiState.filteredRooms,
                isLoading = uiState.isLoading,
                onRoomClick = onRoomClick
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRoomsScreen() {
    OrarUbbFmiTheme {
        FreeRoomsScreen(
            onRoomClick = {},
            onStartHourChange = {},
            onStartMinuteChange = {},
            onEndHourChange = {},
            onEndMinuteChange = {},
            onSelectDays = {},
            onBack = {},
            uiState = FreeRoomsUiState(
                isLoading = false,
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
