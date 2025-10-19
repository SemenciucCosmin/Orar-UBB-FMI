package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.form.ui.components.StudyLinesFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable route with study lines that are selectable for timetable configuration
 */
@Composable
fun StudyLinesFormRoute(navController: NavController) {
    val viewModel = koinViewModel<StudyLinesFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val yearTitle = uiState.year?.let { "$it - ${it.inc()}" }
    val semesterTitle = uiState.semester?.let { stringResource(it.labelRes) }
    val title = when {
        yearTitle != null && semesterTitle != null -> "$yearTitle, $semesterTitle"
        else -> String.BLANK
    }

    StudyLinesFormScreen(
        title = title,
        uiState = uiState,
        onStudyLineClick = viewModel::selectFieldId,
        onStudyLevelClick = viewModel::selectStudyLevel,
        onSelectFilter = viewModel::selectDegreeFilter,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
        onNextClick = viewModel::finishSelection
    )

    EventHandler(viewModel.events) { event ->
        when (event) {
            StudyLinesFormUiState.StudyLinesFormUiEvent.SELECTION_DONE -> {
                viewModel.unregisterEvent(event)
                navController.navigate(ConfigurationFormNavDestination.GroupsForm)
            }
        }
    }
}
