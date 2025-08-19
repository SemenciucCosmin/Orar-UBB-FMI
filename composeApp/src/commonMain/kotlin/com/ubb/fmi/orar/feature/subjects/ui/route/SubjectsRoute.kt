package com.ubb.fmi.orar.feature.subjects.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.subjects.ui.components.SubjectsScreen
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.SubjectsViewModel
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SubjectsRoute(navController: NavController) {
    val viewModel: SubjectsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    SubjectsScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        onChangeSearchQuery = viewModel::setSearchQuery,
        onSubjectClick = { roomId ->
            navController.navigate(TimetableNavDestination.SubjectTimetable(roomId))
        }
    )
}
