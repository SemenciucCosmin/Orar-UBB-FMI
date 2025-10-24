package com.ubb.fmi.orar.feature.groups.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.groups.ui.components.GroupsScreen
import com.ubb.fmi.orar.feature.groups.ui.viewmodel.GroupsViewModel
import com.ubb.fmi.orar.ui.navigation.destination.ExploreNavDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

/**
 * Composable function that represents the route for displaying groups in a timetable.
 * It initializes the GroupsViewModel with the provided field and study level IDs,
 * collects the UI state, and displays the GroupsScreen with the necessary callbacks.
 *
 * @param navController The NavController for navigating between screens.
 * @param fieldId The ID of the field of study.
 * @param studyLevelId The ID of the study level (e.g., Bachelor, Master).
 */
@Composable
fun GroupsRoute(
    navController: NavController,
    fieldId: String,
    studyLevelId: String,
) {
    val viewModel = koinViewModel<GroupsViewModel>(
        parameters = { parametersOf(fieldId, studyLevelId) }
    )

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    GroupsScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        onBack = navController::navigateUp,
        onGroupClick = { groupId ->
            viewModel.handleClickAction()
            navController.navigate(
                ExploreNavDestination.StudyLineTimetable(
                    fieldId = fieldId,
                    studyLevelId = studyLevelId,
                    groupId = groupId
                )
            )
        },
    )
}
