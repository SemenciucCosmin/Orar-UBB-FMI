package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ubb.fmi.orar.feature.form.ui.components.StudyGroupsFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyGroupsFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun StudyGroupsRoute() {
    val viewModel = koinViewModel<StudyGroupsFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyGroupsFormScreen(
        uiState = uiState,
        onStudyGroupClick = viewModel::selectStudyGroup,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry
    )
}
