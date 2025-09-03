package com.ubb.fmi.orar.feature.teachers.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.teachers.ui.components.TeachersScreen
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.TeachersViewModel
import com.ubb.fmi.orar.ui.navigation.components.TimetableBottomBar
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun TeachersRoute(navController: NavController) {
    val viewModel: TeachersViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    TeachersScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        onSelectFilter = viewModel::selectTeacherTitleFilter,
        bottomBar = { TimetableBottomBar(navController) },
        onTeacherClick = { roomId ->
            navController.navigate(TimetableNavDestination.TeacherTimetable(roomId))
        }
    )
}
