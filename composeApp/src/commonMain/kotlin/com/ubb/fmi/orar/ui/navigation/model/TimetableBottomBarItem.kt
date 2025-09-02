package com.ubb.fmi.orar.ui.navigation.model

import com.ubb.fmi.orar.ui.navigation.destination.TimetableNavDestination
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_building
import orar_ubb_fmi.composeapp.generated.resources.ic_home
import orar_ubb_fmi.composeapp.generated.resources.ic_student
import orar_ubb_fmi.composeapp.generated.resources.ic_subject
import orar_ubb_fmi.composeapp.generated.resources.ic_teacher
import orar_ubb_fmi.composeapp.generated.resources.lbl_home
import orar_ubb_fmi.composeapp.generated.resources.lbl_rooms
import orar_ubb_fmi.composeapp.generated.resources.lbl_students
import orar_ubb_fmi.composeapp.generated.resources.lbl_subjects
import orar_ubb_fmi.composeapp.generated.resources.lbl_teachers
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

enum class TimetableBottomBarItem(
    val labelRes: StringResource,
    val icon: DrawableResource,
    val destination: TimetableNavDestination,
) {
    HOME(
        labelRes = Res.string.lbl_home,
        icon = Res.drawable.ic_home,
        destination = TimetableNavDestination.UserTimetable,
    ),
    STUDENTS(
        labelRes = Res.string.lbl_students,
        icon = Res.drawable.ic_student,
        destination = TimetableNavDestination.StudyLines,
    ),
    TEACHERS(
        labelRes = Res.string.lbl_teachers,
        icon = Res.drawable.ic_teacher,
        destination = TimetableNavDestination.Teachers,
    ),
    SUBJECTS(
        labelRes = Res.string.lbl_subjects,
        icon = Res.drawable.ic_subject,
        destination = TimetableNavDestination.Subjects,
    ),
    ROOMS(
        labelRes = Res.string.lbl_rooms,
        icon = Res.drawable.ic_building,
        destination = TimetableNavDestination.Rooms,
    )
}
