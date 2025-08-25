package com.ubb.fmi.orar.ui.navigation.model

import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_building
import orar_ubb_fmi.composeapp.generated.resources.ic_home
import orar_ubb_fmi.composeapp.generated.resources.ic_student
import orar_ubb_fmi.composeapp.generated.resources.ic_subject
import orar_ubb_fmi.composeapp.generated.resources.ic_teacher
import org.jetbrains.compose.resources.DrawableResource

enum class TimetableBottomBarItem(
    val label: String,
    val icon: DrawableResource,
    val destination: TimetableNavDestination,
) {
    HOME(
        label = "Home",
        icon = Res.drawable.ic_home,
        destination = TimetableNavDestination.UserTimetable,
    ),
    STUDENTS(
        label = "Studenti",
        icon = Res.drawable.ic_student,
        destination = TimetableNavDestination.StudyLines,
    ),
    TEACHERS(
        label = "Profesori",
        icon = Res.drawable.ic_teacher,
        destination = TimetableNavDestination.Teachers,
    ),
    SUBJECTS(
        label = "Discipline",
        icon = Res.drawable.ic_subject,
        destination = TimetableNavDestination.Subjects,
    ),
    ROOMS(
        label = "Sali",
        icon = Res.drawable.ic_building,
        destination = TimetableNavDestination.Rooms,
    );
}
