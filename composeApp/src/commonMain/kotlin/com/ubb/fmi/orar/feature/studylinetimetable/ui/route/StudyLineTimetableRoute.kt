package com.ubb.fmi.orar.feature.studylinetimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel.StudyLineTimetableViewModel
import com.ubb.fmi.orar.feature.timetable.ui.components.TimetableScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StudyLineTimetableRoute(
    navController: NavController,
    studyLineId: String,
    studyGroupId: String
) {
    val viewModel: StudyLineTimetableViewModel = koinViewModel(
        parameters = { parametersOf(studyLineId, studyGroupId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onFrequencyClick = viewModel::selectFrequency,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp
    )
}
