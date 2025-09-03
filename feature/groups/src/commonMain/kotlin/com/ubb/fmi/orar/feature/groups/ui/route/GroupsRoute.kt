package com.ubb.fmi.orar.feature.groups.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.groups.ui.components.GroupsScreen
import com.ubb.fmi.orar.feature.groups.ui.viewmodel.GroupsViewModel
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

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
        onGroupClick = { group ->
            navController.navigate(
                TimetableNavDestination.StudyLineTimetable(
                    fieldId = fieldId,
                    studyLevelId = studyLevelId,
                    groupId = group
                )
            )
        },
    )
}
