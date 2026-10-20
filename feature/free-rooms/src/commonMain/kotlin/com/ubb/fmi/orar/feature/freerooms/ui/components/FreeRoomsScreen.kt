package com.ubb.fmi.orar.feature.freerooms.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.model.FreeRoomsUiState
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms
import org.jetbrains.compose.resources.stringResource

/**
 * Composable function that displays the rooms free during a selected schedule.
 */
@Suppress("LongParameterList")
@Composable
fun FreeRoomsScreen(
    uiState: FreeRoomsUiState,
    onRoomClick: (String) -> Unit,
    onStartHourChange: (Int) -> Unit,
    onStartMinuteChange: (Int) -> Unit,
    onEndHourChange: (Int) -> Unit,
    onEndMinuteChange: (Int) -> Unit,
    onSelectDays: (List<Day>) -> Unit,
    onSelectFrequency: (Frequency) -> Unit,
    onNowClick: () -> Unit,
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
            FreeRoomsFilters(
                uiState = uiState,
                onSelectDays = onSelectDays,
                onSelectFrequency = onSelectFrequency,
                onStartHourChange = onStartHourChange,
                onStartMinuteChange = onStartMinuteChange,
                onEndHourChange = onEndHourChange,
                onEndMinuteChange = onEndMinuteChange,
                onNowClick = onNowClick,
                modifier = Modifier
                    .padding(horizontal = Pds.spacing.Medium)
                    .padding(top = Pds.spacing.Small)
            )

            FreeRoomsResults(
                uiState = uiState,
                onRoomClick = onRoomClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewFreeRoomsScreen() {
    OrarUbbFmiTheme {
        FreeRoomsScreen(
            onRoomClick = {},
            onStartHourChange = {},
            onStartMinuteChange = {},
            onEndHourChange = {},
            onEndMinuteChange = {},
            onSelectDays = {},
            onSelectFrequency = {},
            onNowClick = {},
            onBack = {},
            uiState = FreeRoomsUiState(
                isLoading = false,
                selectedDays = persistentListOf(Day.MONDAY),
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
