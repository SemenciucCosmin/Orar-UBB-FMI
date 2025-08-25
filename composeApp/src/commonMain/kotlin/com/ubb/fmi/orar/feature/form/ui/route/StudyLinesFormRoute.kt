package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.StudyLinesFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudyLinesRoute(navController: NavController) {
    val viewModel = koinViewModel<StudyLinesFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyLinesFormScreen(
        uiState = uiState,
        onStudyLineClick = viewModel::selectStudyLineBaseId,
        onStudyYearClick = viewModel::selectStudyYear,
        onSelectFilter = viewModel::selectDegreeFilter,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry
    )

    EventHandler(viewModel.events) { event ->
        when(event) {
            StudyLinesFormUiState.StudyLinesFormEvent.SELECTION_DONE -> {
                viewModel.unregisterEvent(event)
                navController.navigate(ConfigurationFormNavDestination.StudyGroupsForm)
            }
        }
    }
}
