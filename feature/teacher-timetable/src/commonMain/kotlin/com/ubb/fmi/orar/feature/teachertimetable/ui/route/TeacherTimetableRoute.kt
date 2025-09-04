package com.ubb.fmi.orar.feature.teachertimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.TeacherTimetableViewModel
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableFrequencyTab
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableScreen
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Composable function that represents the Teacher Timetable route.
 *
 * This function initializes the [TeacherTimetableViewModel] with the provided [teacherId]
 * and observes its UI state. It displays the timetable screen with a top bar that includes
 * a title and a frequency tab for selecting timetable frequencies.
 *
 * @param navController The navigation controller to handle navigation actions.
 * @param teacherId The ID of the teacher whose timetable is being displayed.
 */
@Composable
fun TeacherTimetableRoute(
    navController: NavController,
    teacherId: String,
) {
    val viewModel: TeacherTimetableViewModel = koinViewModel(
        parameters = { parametersOf(teacherId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        topBar = {
            if (uiState.title.isNotBlank()) {
                TopBar(
                    title = uiState.title,
                    onBack = navController::navigateUp,
                    trailingContent = {
                        TimetableFrequencyTab(
                            selectedFrequency = uiState.selectedFrequency,
                            onFrequencyClick = viewModel::selectFrequency
                        )
                    }
                )
            }
        }
    )
}
