package com.ubb.fmi.orar.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.rooms.ui.route.RoomsRoute
import com.ubb.fmi.orar.feature.roomtimetable.ui.route.RoomTimetableRoute
import com.ubb.fmi.orar.feature.studygroups.ui.route.StudyGroupsRoute
import com.ubb.fmi.orar.feature.studylines.ui.route.StudyLinesRoute
import com.ubb.fmi.orar.feature.studylinetimetable.ui.route.StudyLineTimetableRoute
import com.ubb.fmi.orar.feature.subjects.ui.route.SubjectsRoute
import com.ubb.fmi.orar.feature.subjecttimetable.ui.route.SubjectTimetableRoute
import com.ubb.fmi.orar.feature.teachers.ui.route.TeachersRoute
import com.ubb.fmi.orar.feature.teachertimetable.ui.route.TeacherTimetableRoute
import com.ubb.fmi.orar.feature.timetable.ui.route.HomeRoute
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination

@Composable
fun TimetableGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = TimetableNavDestination.Home,
    ) {
        composable<TimetableNavDestination.Home> { navBackStackEntry ->
            HomeRoute(navController)
        }

        composable<TimetableNavDestination.StudyLines> { navBackStackEntry ->
            StudyLinesRoute(navController)
        }

        composable<TimetableNavDestination.StudyGroups> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<TimetableNavDestination.StudyGroups>()

            StudyGroupsRoute(
                navController = navController,
                studyLineId = args.studyLineId
            )
        }

        composable<TimetableNavDestination.StudyLineTimetable> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<TimetableNavDestination.StudyLineTimetable>()

            StudyLineTimetableRoute(
                navController = navController,
                studyLineId = args.studyLineId,
                studyGroupId = args.studyGroupId,
            )
        }

        composable<TimetableNavDestination.Teachers> { navBackStackEntry ->
            TeachersRoute(navController)
        }

        composable<TimetableNavDestination.TeacherTimetable> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<TimetableNavDestination.TeacherTimetable>()

            TeacherTimetableRoute(
                navController = navController,
                teacherId = args.teacherId
            )
        }

        composable<TimetableNavDestination.Subjects> { navBackStackEntry ->
            SubjectsRoute(navController)
        }

        composable<TimetableNavDestination.SubjectTimetable> { navBackStackEntry ->
            val args = navBackStackEntry.toRoute<TimetableNavDestination.SubjectTimetable>()

            SubjectTimetableRoute(
                navController = navController,
                subjectId = args.subjectId
            )
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