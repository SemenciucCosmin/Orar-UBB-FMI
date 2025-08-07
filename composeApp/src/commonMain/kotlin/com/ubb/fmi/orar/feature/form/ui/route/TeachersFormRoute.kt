package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.TeachersFormScreen
import com.ubb.fmi.orar.feature.form.viewmodel.TeachersFormViewModel
import com.ubb.fmi.orar.feature.form.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.ui.catalog.components.EventHandler
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TeachersFormRoute(teacherTitleId: String) {
    val viewModel = koinViewModel<TeachersFormViewModel>(
        parameters = { parametersOf(teacherTitleId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachersFormScreen(
        uiState = uiState,
        onTeacherClick = viewModel::selectTeacher,
        onNextClick = viewModel::finishSelection,
        onRetryClick = viewModel::retry
    )
}
