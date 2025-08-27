package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.StudyLinesFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StudyLinesFormRoute(
    navController: NavController,
    year: Int,
    semesterId: String,
) {
    val viewModel = koinViewModel<StudyLinesFormViewModel>(
        parameters = { parametersOf(year, semesterId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyLinesFormScreen(
        uiState = uiState,
        onStudyLineClick = viewModel::selectStudyLineBaseId,
        onStudyYearClick = viewModel::selectStudyYear,
        onSelectFilter = viewModel::selectDegreeFilter,
        onRetryClick = viewModel::retry,
        onNextClick = {
            val studyLineBaseId = uiState.selectedStudyLineBaseId ?: return@StudyLinesFormScreen
            val studyLineYearId = uiState.selectedStudyYearId ?: return@StudyLinesFormScreen
            val studyLine = uiState.filteredGroupedStudyLines.flatten().firstOrNull {
                it.baseId == studyLineBaseId && it.studyYearId == studyLineYearId
            } ?: return@StudyLinesFormScreen

            navController.navigate(
                ConfigurationFormNavDestination.StudyGroupsForm(
                    year = year,
                    semesterId = semesterId,
                    studyLineBaseId = studyLineBaseId,
                    studyLineYearId = studyLineYearId,
                    studyLineDegreeId = studyLine.degreeId,
                )
            )
        }
    )
}
