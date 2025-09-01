package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.StudyLinesFormScreen
import com.ubb.fmi.orar.feature.form.ui.model.Semester
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyLinesFormViewModel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.ui.navigation.destination.ConfigurationFormNavDestination
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StudyLinesFormRoute(
    navController: NavController,
    year: Int,
    semesterId: String,
) {
    val semester = Semester.getById(semesterId)
    val viewModel = koinViewModel<StudyLinesFormViewModel>(
        parameters = { parametersOf(year, semesterId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyLinesFormScreen(
        title = "$year - ${year.inc()}, ${stringResource(semester.labelRes)}",
        uiState = uiState,
        onStudyLineClick = viewModel::selectFieldId,
        onStudyLevelClick = viewModel::selectStudyLevel,
        onSelectFilter = viewModel::selectDegreeFilter,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
        onNextClick = {
            val fieldId = uiState.selectedFieldId ?: return@StudyLinesFormScreen
            val studyLevelId = uiState.selectedStudyLevelId ?: return@StudyLinesFormScreen
            val studyLine = uiState.filteredGroupedStudyLines.flatten().firstOrNull {
                it.fieldId == fieldId && it.levelId == studyLevelId
            } ?: return@StudyLinesFormScreen

            navController.navigate(
                ConfigurationFormNavDestination.GroupsForm(
                    year = year,
                    semesterId = semesterId,
                    fieldId = fieldId,
                    studyLevelId = studyLevelId,
                    studyLineDegreeId = studyLine.degreeId,
                )
            )
        }
    )
}
