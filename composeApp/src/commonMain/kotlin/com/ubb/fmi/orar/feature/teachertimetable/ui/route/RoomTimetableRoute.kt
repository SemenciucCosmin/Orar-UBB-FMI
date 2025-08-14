package com.ubb.fmi.orar.feature.teachertimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import com.ubb.fmi.orar.feature.teachertimetable.ui.viewmodel.TeacherTimetableViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun TeacherTimetableRoute(
    navController: NavController,
    teacherId: String,
) {
    val viewModel: TeacherTimetableViewModel = koinViewModel(
        parameters = { parametersOf(teacherId) }
    )

}
