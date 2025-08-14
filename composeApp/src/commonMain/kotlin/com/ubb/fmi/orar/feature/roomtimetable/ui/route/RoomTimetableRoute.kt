package com.ubb.fmi.orar.feature.roomtimetable.ui.route

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.ubb.fmi.orar.feature.roomtimetable.ui.viewmodel.RoomTimetableViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RoomTimetableRoute(
    navController: NavController,
    roomId: String,
) {
    val viewModel: RoomTimetableViewModel = koinViewModel(parameters = { parametersOf(roomId) })

}
