package com.ubb.fmi.orar.feature.roomtimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import com.ubb.fmi.orar.ui.catalog.components.TimetableScreen
import com.ubb.fmi.orar.ui.catalog.components.TimetableTopBar
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RoomTimetableRoute(
    navController: NavController,
    roomId: String,
) {
    val viewModel: RoomTimetableViewModel = koinViewModel(parameters = { parametersOf(roomId) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        topBar = {
            uiState.timetable?.let { timetable ->
                TimetableTopBar(
                    title = timetable.title,
                    subtitle = timetable.subtitle,
                    selectedFrequency = uiState.selectedFrequency,
                    onFrequencyClick = viewModel::selectFrequency,
                    onBack = navController::navigateUp
                )
            }
        }
    )
}
