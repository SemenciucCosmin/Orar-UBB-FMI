package com.ubb.fmi.orar.feature.rooms.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.rooms.ui.components.RoomsScreen
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.RoomsViewModel
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RoomsRoute(navController: NavController) {
    val viewModel: RoomsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoomsScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        bottomBar = { TimetableBottomBar(navController) },
        onRoomClick = { roomId ->
            navController.navigate(TimetableNavDestination.RoomTimetable(roomId))
        }
    )
}
