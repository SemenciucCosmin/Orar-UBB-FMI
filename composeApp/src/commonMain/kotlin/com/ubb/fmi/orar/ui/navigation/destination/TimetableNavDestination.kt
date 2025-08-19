package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class TimetableNavDestination() {

    @Serializable
    data object Home: TimetableNavDestination()

    @Serializable
    data object Students: TimetableNavDestination()

    @Serializable
    data object Teachers: TimetableNavDestination()

    @Serializable
    data class TeacherTimetable(val teacherId: String): TimetableNavDestination()

    @Serializable
    data object Subjects: TimetableNavDestination()

    @Serializable
    data class SubjectTimetable(val subjectId: String): TimetableNavDestination()

    @Serializable
    data object Rooms: TimetableNavDestination()

    @Serializable
    data class RoomTimetable(val roomId: String): TimetableNavDestination()
}
