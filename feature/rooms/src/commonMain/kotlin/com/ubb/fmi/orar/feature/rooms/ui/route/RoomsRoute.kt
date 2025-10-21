package com.ubb.fmi.orar.feature.rooms.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.rooms.ui.components.RoomsScreen
import com.ubb.fmi.orar.feature.rooms.ui.viewmodel.RoomsViewModel
import com.ubb.fmi.orar.ui.navigation.destination.ExploreNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the Rooms route in the application.
 * It initializes the RoomsViewModel and observes its UI state.
 *
 * @param navController The NavController used for navigation within the app.
 */
@Composable
fun RoomsRoute(navController: NavController) {
    val viewModel: RoomsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoomsScreen(
        uiState = uiState,
        onChangeSearchQuery = viewModel::setSearchQuery,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
        onRoomClick = { roomId ->
            navController.navigate(ExploreNavDestination.RoomTimetable(roomId))
        }
    )
}
