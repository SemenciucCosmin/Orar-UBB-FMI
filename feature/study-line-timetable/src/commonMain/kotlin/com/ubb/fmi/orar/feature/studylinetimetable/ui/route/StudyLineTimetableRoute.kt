package com.ubb.fmi.orar.feature.studylinetimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.studylinetimetable.ui.viewmodel.StudyLineTimetableViewModel
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableFrequencyTab
import com.ubb.fmi.orar.ui.catalog.components.timetable.TimetableScreen
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Composable function that represents the Study Line Timetable route.
 *
 * This function initializes the [StudyLineTimetableViewModel] and observes its UI state.
 * It displays a timetable screen with a top bar that includes the title, back navigation,
 * and a frequency tab for selecting timetable frequencies.
 *
 * @param navController The navigation controller for handling navigation actions.
 * @param fieldId The ID of the field of study.
 * @param studyLevelId The ID of the study level.
 * @param groupId The ID of the group.
 */
@Composable
fun StudyLineTimetableRoute(
    navController: NavController,
    fieldId: String,
    studyLevelId: String,
    groupId: String
) {
    val viewModel: StudyLineTimetableViewModel = koinViewModel(
        parameters = { parametersOf(fieldId, studyLevelId, groupId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TimetableScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        topBar = {
            if (uiState.title.isNotBlank()) {
                TopBar(
                    title = uiState.title,
                    onBack = navController::navigateUp,
                    trailingContent = {
                        TimetableFrequencyTab(
                            selectedFrequency = uiState.selectedFrequency,
                            onFrequencyClick = viewModel::selectFrequency
                        )
                    },
                    subtitle = uiState.studyLevel?.let {
                        "${stringResource(it.labelRes)} - ${uiState.group}"
                    }
                )
            }
        }
    )
}
