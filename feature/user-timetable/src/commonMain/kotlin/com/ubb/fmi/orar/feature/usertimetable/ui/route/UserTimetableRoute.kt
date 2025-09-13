package com.ubb.fmi.orar.feature.usertimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.usertimetable.ui.components.UserTimetableTopBar
import com.ubb.fmi.orar.feature.usertimetable.ui.viewmodel.UserTimetableViewModel
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableScreen
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar
import com.ubb.fmi.orar.ui.navigation.destination.SettingsNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the User Timetable route.
 *
 * This function sets up the User Timetable screen with its ViewModel and UI state.
 * It includes a top bar for frequency selection and edit mode, and a bottom bar for navigation.
 *
 * @param navController The NavController used for navigation within the app.
 */
@Composable
fun UserTimetableRoute(navController: NavController) {
    val viewModel: UserTimetableViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        bottomBar = { TimetableBottomBar(navController) },
        onItemVisibilityChange = viewModel::changeTimetableClassVisibility,
        topBar = {
            if (!uiState.isLoading && !uiState.isError) {
                UserTimetableTopBar(
                    isEditModeOn = uiState.isEditModeOn,
                    selectedFrequency = uiState.selectedFrequency,
                    onFrequencyClick = viewModel::selectFrequency,
                    onEditClick = viewModel::changeEditMode,
                    onSettingsClick = { navController.navigate(SettingsNavDestination.Settings) }
                )
            }
        }
    )
}
