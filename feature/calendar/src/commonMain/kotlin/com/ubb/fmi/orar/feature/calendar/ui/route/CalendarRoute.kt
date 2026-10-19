package com.ubb.fmi.orar.feature.calendar.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.calendar.ui.components.CalendarScreen
import com.ubb.fmi.orar.feature.calendar.ui.viewmodel.CalendarViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the Calendar route in the application.
 */
@Composable
fun CalendarRoute(navController: NavController) {
    val viewModel: CalendarViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
    )
}
