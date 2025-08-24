package com.ubb.fmi.orar.feature.roomtimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.roomtimetable.ui.components.RoomTimetableScreen
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RoomTimetableRoute(
    navController: NavController,
    roomId: String,
) {
    val viewModel: RoomTimetableViewModel = koinViewModel(parameters = { parametersOf(roomId) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RoomTimetableScreen(
        uiState = uiState,
        onFrequencyClick = viewModel::selectFrequency,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp
    )
}
