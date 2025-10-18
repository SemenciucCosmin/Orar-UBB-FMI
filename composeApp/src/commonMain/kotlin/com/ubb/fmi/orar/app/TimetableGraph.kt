package com.ubb.fmi.orar.app

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.groups.ui.route.GroupsRoute
import com.ubb.fmi.orar.feature.rooms.ui.route.RoomsRoute
import com.ubb.fmi.orar.feature.roomtimetable.ui.route.RoomTimetableRoute
import com.ubb.fmi.orar.feature.startup.ui.route.StartupRoute
import com.ubb.fmi.orar.feature.studylines.ui.route.StudyLinesRoute
import com.ubb.fmi.orar.feature.grouptimetable.ui.route.GroupTimetableRoute
import com.ubb.fmi.orar.feature.subjects.ui.route.SubjectsRoute
import com.ubb.fmi.orar.feature.subjectstimetable.ui.route.SubjectTimetableRoute
import com.ubb.fmi.orar.feature.teachers.ui.route.TeachersRoute
import com.ubb.fmi.orar.feature.teachertimetable.ui.route.TeacherTimetableRoute
import com.ubb.fmi.orar.feature.usertimetable.ui.route.UserTimetableRoute
import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination

/**
 * Navigation graph for the timetable feature in the Orar UBB FMI application.
 * This function defines the routes for various timetable-related screens such as user timetable,
 * study lines, groups, teachers, subjects, and rooms.
 * Each screen is represented by a composable function that handles the specific route logic.
 */
fun NavGraphBuilder.timetableGraph(navController: NavController) {
    composable<TimetableNavDestination.Startup> { navBackStackEntry ->
        StartupRoute(navController)
    }

    composable<TimetableNavDestination.UserTimetable> { navBackStackEntry ->
        UserTimetableRoute(navController)
    }

    composable<TimetableNavDestination.StudyLines> { navBackStackEntry ->
        StudyLinesRoute(navController)
    }

    composable<TimetableNavDestination.Groups> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<TimetableNavDestination.Groups>()

        GroupsRoute(
            navController = navController,
            fieldId = args.fieldId,
            studyLevelId = args.studyLevelId
        )
    }

    composable<TimetableNavDestination.StudyLineTimetable> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<TimetableNavDestination.StudyLineTimetable>()

        GroupTimetableRoute(
            navController = navController,
            fieldId = args.fieldId,
            studyLevelId = args.studyLevelId,
            groupId = args.groupId,
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