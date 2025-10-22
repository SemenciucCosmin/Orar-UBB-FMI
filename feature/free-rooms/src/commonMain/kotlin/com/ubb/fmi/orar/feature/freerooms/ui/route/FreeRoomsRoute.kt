package com.ubb.fmi.orar.feature.freerooms.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.freerooms.ui.components.FreeRoomsScreen
import com.ubb.fmi.orar.feature.freerooms.ui.viewmodel.FreeRoomsViewModel
import com.ubb.fmi.orar.ui.navigation.destination.ExploreNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the Rooms route in the application.
 * It initializes the RoomsViewModel and observes its UI state.
 */
@Composable
fun FreeRoomsRoute(navController: NavController) {
    val viewModel: FreeRoomsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    FreeRoomsScreen(
        uiState = uiState,
        onBack = navController::navigateUp,
        onStartHourChange = viewModel::changeStartHour,
        onStartMinuteChange = viewModel::changeStartMinute,
        onEndHourChange = viewModel::changeEndHour,
        onEndMinuteChange = viewModel::changeEndMinute,
        onSelectDays = viewModel::selectDays,
        onRoomClick = { roomId ->
            navController.navigate(ExploreNavDestination.RoomTimetable(roomId))
        }
    )
}
