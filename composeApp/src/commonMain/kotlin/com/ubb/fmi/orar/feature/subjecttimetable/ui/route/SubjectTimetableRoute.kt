package com.ubb.fmi.orar.feature.subjecttimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.subjecttimetable.ui.components.SubjectTimetableScreen
import com.ubb.fmi.orar.feature.subjecttimetable.ui.viewmodel.SubjectTimetableViewModel
import com.ubb.fmi.orar.feature.teachertimetable.ui.components.TeacherTimetableScreen
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.TeacherTimetableViewModel
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

    SubjectTimetableScreen(
        uiState = uiState,
        onFrequencyClick = viewModel::selectFrequency,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp
    )
}
