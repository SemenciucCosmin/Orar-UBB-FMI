package com.ubb.fmi.orar.feature.rooms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState.Companion.filteredRooms
import com.ubb.fmi.orar.ui.catalog.components.SearchBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_location
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_rooms
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays a list of rooms.
 * @param uiState The state of the rooms UI, containing the list of rooms and loading/error states.
 * @param onRoomClick Callback function to be invoked when a room is clicked, passing the room ID.
 * @param onRetryClick Callback function to be invoked when the user clicks the retry button in case of an error.
 * @param bottomBar Composable function for the bottom bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoomsScreen(
    uiState: RoomsUiState,
    onRoomClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    onChangeSearchQuery: (String) -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        isEmpty = uiState.isEmpty,
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        bottomBar = bottomBar,
        topBar = {
            if (uiState.errorStatus == null && !uiState.isLoading) {
                TopAppBar(
                    title = {
                        SearchBar(
                            value = uiState.searchQuery,
                            onValueChange = onChangeSearchQuery,
                            placeholder = stringResource(Res.string.lbl_rooms),
                            onClearClick = { onChangeSearchQuery(String.BLANK) },
                            modifier = Modifier.padding(end = Pds.spacing.Medium)
                        )
                    }
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(Pds.spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(uiState.filteredRooms) { room ->
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

@Preview
@Composable
private fun PreviewRoomsScreen() {
    OrarUbbFmiTheme {
        RoomsScreen(
            onRoomClick = {},
            onRetryClick = {},
            onChangeSearchQuery = {},
            bottomBar = {},
            uiState = RoomsUiState(
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
