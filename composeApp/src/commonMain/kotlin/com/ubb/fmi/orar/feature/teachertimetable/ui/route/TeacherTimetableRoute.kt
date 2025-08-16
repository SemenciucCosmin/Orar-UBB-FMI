package com.ubb.fmi.orar.feature.teachertimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.teachertimetable.ui.components.TeacherTimetableScreen
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.TeacherTimetableViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TeacherTimetableRoute(
    navController: NavController,
    teacherId: String,
) {
    val viewModel: TeacherTimetableViewModel = koinViewModel(
        parameters = { parametersOf(teacherId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeacherTimetableScreen(
        uiState = uiState,
        onFrequencyClick = viewModel::selectFrequency,
        onRetryClick = viewModel::retry
    )
}
