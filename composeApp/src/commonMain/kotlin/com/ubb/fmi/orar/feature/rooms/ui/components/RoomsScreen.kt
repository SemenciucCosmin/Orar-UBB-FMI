package com.ubb.fmi.orar.feature.rooms.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@Composable
fun RoomsScreen(
    uiState: RoomsUiState,
    onRoomClick: (String) -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        when {
            uiState.isLoading -> {
                ProgressOverlay(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            uiState.isError -> {
                FailureState(
                    onRetry = onRetryClick,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)
                        .padding(paddingValues)
                ) {
                    items(uiState.rooms) { room ->
                        RoomListItem(
                            name = room.name,
                            location = room.location,
                            onClick = { onRoomClick(room.id) },
                        )
                    }
                }
            }
        }
    }
}