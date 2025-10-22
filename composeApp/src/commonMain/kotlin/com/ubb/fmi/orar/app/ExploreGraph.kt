package com.ubb.fmi.orar.app

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.ubb.fmi.orar.feature.freerooms.ui.route.FreeRoomsRoute
import com.ubb.fmi.orar.feature.groups.ui.route.GroupsRoute
import com.ubb.fmi.orar.feature.grouptimetable.ui.route.GroupTimetableRoute
import com.ubb.fmi.orar.feature.rooms.ui.route.RoomsRoute
import com.ubb.fmi.orar.feature.roomtimetable.ui.route.RoomTimetableRoute
import com.ubb.fmi.orar.feature.studylines.ui.route.StudyLinesRoute
import com.ubb.fmi.orar.feature.subjects.ui.route.SubjectsRoute
import com.ubb.fmi.orar.feature.subjectstimetable.ui.route.SubjectTimetableRoute
import com.ubb.fmi.orar.feature.teachers.ui.route.TeachersRoute
import com.ubb.fmi.orar.feature.teachertimetable.ui.route.TeacherTimetableRoute
import com.ubb.fmi.orar.ui.navigation.destination.ExploreNavDestination

/**
 * Navigation graph for the explore feature in the Orar UBB FMI application.
 */
fun NavGraphBuilder.exploreGraph(navController: NavController) {
    composable<ExploreNavDestination.StudyLines> { navBackStackEntry ->
        StudyLinesRoute(navController)
    }

    composable<ExploreNavDestination.Groups> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ExploreNavDestination.Groups>()

        GroupsRoute(
            navController = navController,
            fieldId = args.fieldId,
            studyLevelId = args.studyLevelId
        )
    }

    composable<ExploreNavDestination.StudyLineTimetable> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ExploreNavDestination.StudyLineTimetable>()

        GroupTimetableRoute(
            navController = navController,
            fieldId = args.fieldId,
            studyLevelId = args.studyLevelId,
            groupId = args.groupId,
        )
    }

    composable<ExploreNavDestination.Teachers> { navBackStackEntry ->
        TeachersRoute(navController)
    }

    composable<ExploreNavDestination.TeacherTimetable> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ExploreNavDestination.TeacherTimetable>()

        TeacherTimetableRoute(
            navController = navController,
            teacherId = args.teacherId
        )
    }

    composable<ExploreNavDestination.Subjects> { navBackStackEntry ->
        SubjectsRoute(navController)
    }

    composable<ExploreNavDestination.SubjectTimetable> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ExploreNavDestination.SubjectTimetable>()

        SubjectTimetableRoute(
            navController = navController,
            subjectId = args.subjectId
        )
    }

    composable<ExploreNavDestination.Rooms> { navBackStackEntry ->
        RoomsRoute(navController)
    }

    composable<ExploreNavDestination.RoomTimetable> { navBackStackEntry ->
        val args = navBackStackEntry.toRoute<ExploreNavDestination.RoomTimetable>()

        RoomTimetableRoute(
            navController = navController,
            roomId = args.roomId
        )
    }

    composable<ExploreNavDestination.FreeRooms> { navBackStackEntry ->
        FreeRoomsRoute(navController)
    }
}