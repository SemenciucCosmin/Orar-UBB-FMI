package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.StudyLinesFormScreen
import com.ubb.fmi.orar.feature.form.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudyLinesRoute(navController: NavController) {
    val viewModel = koinViewModel<StudyLinesFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyLinesFormScreen(
        uiState = uiState,
        onStudyLineClick = viewModel::selectStudyLine,
        onStudyYearClick = viewModel::selectStudyYear,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry
    )

    EventHandler(viewModel.events) { event ->
        when(event) {
            StudyLinesFormUiState.StudyLinesFormEvent.SELECTION_DONE -> {
                navController.navigate(ConfigurationFormNavDestination.StudyGroupsForm)
            }
        }
    }
}
