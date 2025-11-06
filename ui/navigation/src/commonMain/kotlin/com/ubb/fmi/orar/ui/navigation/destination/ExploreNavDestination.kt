package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

/**
 * Represents the navigation destinations for the exploring feature in the application.
 * Encapsulates other destinations meant for searching
 */
@Serializable
sealed class ExploreNavDestination {

    /**
     * Represents a study lines destination.
     * This destination is used to navigate to the list of study lines.
     */
    @Serializable
    data object StudyLines : ExploreNavDestination()

    /**
     * Represents a study line timetable destination.
     * This destination is used to navigate to the timetable for a specific study line.
     *
     * @property fieldId The identifier for the field of study.
     * @property studyLevelId The identifier for the study level.
     */
    @Serializable
    data class Groups(
        val fieldId: String,
        val studyLevelId: String
    ) : ExploreNavDestination()

    /**
     * Represents a group timetable destination.
     * This destination is used to navigate to the timetable for a specific group.
     *
     * @property fieldId The identifier for the field of study.
     * @property studyLevelId The identifier for the study level.
     * @property groupId The identifier for the group.
     */
    @Serializable
    data class StudyLineTimetable(
        val fieldId: String,
        val studyLevelId: String,
        val groupId: String
    ) : ExploreNavDestination()

    /**
     * Represents a teachers destination.
     * This destination is used to navigate to the list of teachers.
     */
    @Serializable
    data object Teachers : ExploreNavDestination()

    /**
     * Represents a teacher timetable destination.
     * This destination is used to navigate to the timetable for a specific teacher.
     *
     * @property teacherId The identifier for the teacher.
     */
    @Serializable
    data class TeacherTimetable(val teacherId: String) : ExploreNavDestination()

    /**
     * Represents a subjects destination.
     * This destination is used to navigate to the list of subjects.
     */
    @Serializable
    data object Subjects : ExploreNavDestination()

    /**
     * Represents a subject timetable destination.
     * This destination is used to navigate to the timetable for a specific subject.
     *
     * @property subjectId The identifier for the subject.
     */
    @Serializable
    data class SubjectTimetable(val subjectId: String) : ExploreNavDestination()

    /**
     * Represents a rooms destination.
     * This destination is used to navigate to the list of rooms.
     */
    @Serializable
    data object Rooms : ExploreNavDestination()

    /**
     * Represents a room timetable destination.
     * This destination is used to navigate to the timetable for a specific room.
     *
     * @property roomId The identifier for the room.
     */
    @Serializable
    data class RoomTimetable(val roomId: String) : ExploreNavDestination()

    /**
     * Destination for searching free room
     */
    @Serializable
    data object FreeRooms : ExploreNavDestination()

    /**
     * Represents a navigation destination for the personal event addition feature.
     */
    @Serializable
    data object AddPersonalEvent : ExploreNavDestination()
}
