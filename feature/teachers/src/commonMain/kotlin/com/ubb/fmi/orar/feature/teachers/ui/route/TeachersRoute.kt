package com.ubb.fmi.orar.feature.teachers.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.teachers.ui.components.TeachersScreen
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.TeachersViewModel
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the Teachers route in the application.
 * It initializes the TeachersViewModel and collects its UI state to display the TeachersScreen.
 *
 * @param navController The NavController used for navigation within the app.
 */
@Composable
fun TeachersRoute(navController: NavController) {
    val viewModel: TeachersViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachersScreen(
        uiState = uiState,
        onSelectFilter = viewModel::selectTeacherTitleFilter,
        onChangeSearchQuery = viewModel::setSearchQuery,
        onRetryClick = viewModel::retry,
        bottomBar = { TimetableBottomBar(navController) },
        onTeacherClick = { roomId ->
            navController.navigate(TimetableNavDestination.TeacherTimetable(roomId))
        }
    )
}
