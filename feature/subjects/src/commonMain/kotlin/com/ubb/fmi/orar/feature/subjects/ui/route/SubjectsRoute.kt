package com.ubb.fmi.orar.feature.subjects.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.subjects.ui.components.SubjectsScreen
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.SubjectsViewModel
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the Subjects route.
 * It initializes the [SubjectsViewModel] and collects its UI state.
 * It displays the [SubjectsScreen] with the current UI state and provides callbacks for user interactions
 * such as retrying, changing the search query, and navigating to a subject's timetable.
 * @param navController The [NavController] used for navigation within the app.
 */
@Composable
fun SubjectsRoute(navController: NavController) {
    val viewModel: SubjectsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SubjectsScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        onChangeSearchQuery = viewModel::setSearchQuery,
        bottomBar = { TimetableBottomBar(navController) },
        onSubjectClick = { roomId ->
            navController.navigate(TimetableNavDestination.SubjectTimetable(roomId))
        }
    )
}
