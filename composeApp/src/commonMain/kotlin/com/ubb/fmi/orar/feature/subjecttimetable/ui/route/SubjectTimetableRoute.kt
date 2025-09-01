package com.ubb.fmi.orar.feature.subjecttimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.subjecttimetable.ui.viewmodel.SubjectTimetableViewModel
import com.ubb.fmi.orar.ui.catalog.components.TimetableScreen
import com.ubb.fmi.orar.ui.catalog.components.TimetableTopBar
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun SubjectTimetableRoute(
    navController: NavController,
    subjectId: String,
) {
    val viewModel: SubjectTimetableViewModel = koinViewModel(
        parameters = { parametersOf(subjectId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        topBar = {
            if (uiState.title.isNotBlank()) {
                TimetableTopBar(
                    title = uiState.title,
                    selectedFrequency = uiState.selectedFrequency,
                    onFrequencyClick = viewModel::selectFrequency,
                    onBack = navController::navigateUp,
                    subtitle = uiState.studyLevel?.let {
                        "${stringResource(it.labelRes)} - ${uiState.group}"
                    }
                )
            }
        }
    )
}
