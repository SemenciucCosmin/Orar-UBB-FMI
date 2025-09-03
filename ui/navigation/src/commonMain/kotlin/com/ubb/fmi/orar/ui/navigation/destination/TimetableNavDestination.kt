package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class TimetableNavDestination {

    @Serializable
    data object Startup : TimetableNavDestination()

    @Serializable
    data object UserTimetable : TimetableNavDestination()

    @Serializable
    data object StudyLines : TimetableNavDestination()

    @Serializable
    data class Groups(
        val fieldId: String,
        val studyLevelId: String
    ) : TimetableNavDestination()

    @Serializable
    data class StudyLineTimetable(
        val fieldId: String,
        val studyLevelId: String,
        val groupId: String
    ) : TimetableNavDestination()

    @Serializable
    data object Teachers : TimetableNavDestination()

    @Serializable
    data class TeacherTimetable(val teacherId: String) : TimetableNavDestination()

    @Serializable
    data object Subjects : TimetableNavDestination()

    @Serializable
    data class SubjectTimetable(val subjectId: String) : TimetableNavDestination()

    @Serializable
    data object Rooms : TimetableNavDestination()

    @Serializable
    data class RoomTimetable(val roomId: String) : TimetableNavDestination()
}
