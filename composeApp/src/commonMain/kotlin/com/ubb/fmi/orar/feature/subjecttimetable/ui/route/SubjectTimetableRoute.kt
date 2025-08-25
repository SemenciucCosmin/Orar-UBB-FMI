package com.ubb.fmi.orar.feature.subjecttimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.subjecttimetable.ui.viewmodel.SubjectTimetableViewModel
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SubjectTimetableRoute(
    navController: NavController,
    subjectId: String,
) {
    val viewModel: SubjectTimetableViewModel = koinViewModel(
        parameters = { parametersOf(subjectId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onFrequencyClick = viewModel::selectFrequency,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp
    )
}
