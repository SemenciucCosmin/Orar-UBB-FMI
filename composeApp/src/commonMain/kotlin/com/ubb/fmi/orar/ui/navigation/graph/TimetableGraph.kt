package com.ubb.fmi.orar.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.rooms.ui.route.RoomsRoute
import com.ubb.fmi.orar.feature.roomtimetable.ui.route.RoomTimetableRoute
import com.ubb.fmi.orar.feature.timetable.ui.route.MainTimetableRoute
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination

@Composable
fun TimetableGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = TimetableNavDestination.Rooms,
    ) {
        composable<TimetableNavDestination.Main> { navBackStackEntry ->
            MainTimetableRoute(navController)
        }

        composable<TimetableNavDestination.Rooms> { navBackStackEntry ->
            RoomsRoute(navController)
        }

        composable<TimetableNavDestination.RoomTimetable> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<TimetableNavDestination.RoomTimetable>()

            RoomTimetableRoute(
                navController = navController,
                roomId = args.roomId
            )
        }
    }
}