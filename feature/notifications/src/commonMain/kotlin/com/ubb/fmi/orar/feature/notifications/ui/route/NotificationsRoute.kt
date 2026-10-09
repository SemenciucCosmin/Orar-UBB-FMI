package com.ubb.fmi.orar.feature.notifications.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.notifications.ui.components.NotificationsScreen
import com.ubb.fmi.orar.feature.notifications.ui.viewmodel.NotificationsViewModel
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the notifications settings route in the application.
 * It initializes the NotificationsViewModel and observes its UI state.
 *
 * @param navController The NavController used for navigation within the app.
 */
@Composable
fun NotificationsRoute(navController: NavController) {
    val viewModel: NotificationsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NotificationsScreen(
        uiState = uiState,
        onBack = navController::navigateUp,
        onNotificationsEnabledChange = viewModel::setNotificationsEnabled,
        onNotificationAdvanceMinutesChange = viewModel::setNotificationAdvanceMinutes,
    )
}
