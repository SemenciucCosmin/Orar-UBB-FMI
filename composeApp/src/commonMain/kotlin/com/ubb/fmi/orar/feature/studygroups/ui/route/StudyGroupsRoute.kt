package com.ubb.fmi.orar.feature.studygroups.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.form.ui.components.StudyGroupsFormScreen
import com.ubb.fmi.orar.feature.form.ui.viewmodel.StudyGroupsFormViewModel
import com.ubb.fmi.orar.feature.studygroups.ui.components.StudyGroupsScreen
import com.ubb.fmi.orar.feature.studygroups.ui.viewmodel.StudyGroupsViewModel
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import io.ktor.http.parameters
import io.ktor.http.parametersOf
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun StudyGroupsRoute(
    navController: NavController,
    studyLineId: String,
) {
    val viewModel = koinViewModel<StudyGroupsViewModel>(parameters = { parametersOf(studyLineId) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    StudyGroupsScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        onStudyGroupClick = { studyGroup ->
            navController.navigate(
                TimetableNavDestination.StudyLineTimetable(
                    studyLineId = studyLineId,
                    studyGroupId = studyGroup.id
                )
            )
        },
    )
}
