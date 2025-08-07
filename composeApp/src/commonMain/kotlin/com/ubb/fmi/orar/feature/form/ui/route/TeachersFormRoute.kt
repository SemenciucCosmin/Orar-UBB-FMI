package com.ubb.fmi.orar.feature.form.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.TeachersFormScreen
import com.ubb.fmi.orar.feature.form.viewmodel.TeachersFormViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TeachersFormRoute(
    teacherTitleId: String,
    navController: NavController
) {
    val viewModel = koinViewModel<TeachersFormViewModel>(
        parameters = { parametersOf(teacherTitleId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachersFormScreen(
        uiState = uiState,
        onTeacherClick = viewModel::selectTeacher,
        onNextClick = { },
        onRetry = viewModel::retry
    )
}
