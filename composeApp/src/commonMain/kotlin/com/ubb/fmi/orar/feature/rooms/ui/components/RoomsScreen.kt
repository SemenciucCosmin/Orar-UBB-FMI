package com.ubb.fmi.orar.feature.rooms.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.model.RoomsUiState
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_location
import org.jetbrains.compose.resources.painterResource

@Composable
fun RoomsScreen(
    uiState: RoomsUiState,
    onRoomClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    Scaffold(bottomBar = bottomBar) { paddingValues ->
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
                    contentPadding = PaddingValues(Pds.spacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(uiState.rooms) { room ->
                        ListItemClickable(
                            headline = room.name,
                            underLine = room.location,
                            onClick = { onRoomClick(room.id) },
                            leadingIcon = painterResource(Res.drawable.ic_location),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}