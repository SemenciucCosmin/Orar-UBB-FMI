package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class TimetableNavDestination() {

    @Serializable
    data object Main: TimetableNavDestination()

    @Serializable
    data object Rooms: TimetableNavDestination()

    @Serializable
    data class RoomTimetable(val roomId: String): TimetableNavDestination()

    @Serializable
    data object Teachers: TimetableNavDestination()

    @Serializable
    data class TeacherTimetable(val teacherId: String): TimetableNavDestination()
}
