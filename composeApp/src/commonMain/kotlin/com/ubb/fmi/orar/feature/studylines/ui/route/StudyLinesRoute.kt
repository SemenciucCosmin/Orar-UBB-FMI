package com.ubb.fmi.orar.feature.studylines.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.usertimetable.model.StudyYear
import com.ubb.fmi.orar.feature.studylines.ui.components.StudyLinesScreen
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.StudyLinesViewModel
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudyLinesRoute(navController: NavController) {
    val viewModel = koinViewModel<StudyLinesViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyLinesScreen(
        uiState = uiState,
        onStudyLineClick = viewModel::selectStudyLineBaseId,
        onSelectFilter = viewModel::selectDegreeFilter,
        onRetryClick = viewModel::retry,
        bottomBar = { TimetableBottomBar(navController) },
        onStudyYearClick = { studyYearId ->
            if (uiState.selectedStudyLineBaseId != null) {
                val studyYear = StudyYear.getById(studyYearId)
                val studyLineId = uiState.selectedStudyLineBaseId + studyYear.notation
                navController.navigate(TimetableNavDestination.StudyGroups(studyLineId))
            }
        },
    )
}
