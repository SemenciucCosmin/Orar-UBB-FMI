package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ubb.fmi.orar.feature.form.ui.components.TeachersFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.TeachersFormViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeachersFormRoute() {
    val viewModel = koinViewModel<TeachersFormViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachersFormScreen(
        uiState = uiState,
        onTeacherClick = viewModel::selectTeacher,
        onSelectFilter = viewModel::selectTeacherTitleFilter,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry
    )
}
