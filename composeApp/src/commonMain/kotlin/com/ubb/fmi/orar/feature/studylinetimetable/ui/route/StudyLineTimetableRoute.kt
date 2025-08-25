package com.ubb.fmi.orar.feature.studylinetimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.roomtimetable.ui.components.RoomTimetableScreen
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import com.ubb.fmi.orar.feature.studylinetimetable.ui.components.StudyLineTimetableScreen
import com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel.StudyLineTimetableViewModel
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

    StudyLineTimetableScreen(
        uiState = uiState,
        onFrequencyClick = viewModel::selectFrequency,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp
    )
}
