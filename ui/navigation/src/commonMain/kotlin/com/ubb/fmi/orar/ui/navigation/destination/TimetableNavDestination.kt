package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

/**
 * Represents the navigation destinations for the timetable feature in the application.
 *
 * This sealed class defines various destinations related to timetables, such as user timetables,
 * study lines, groups, teachers, subjects, and rooms. Each destination is represented by a data
 * class with relevant parameters.
 */
@Serializable
sealed class TimetableNavDestination {

    /**
     * Represents a navigation destination for the timetable feature.
     *
     * This sealed class is used to define different types of destinations that can be navigated to
     * within the timetable feature. Each subclass represents a specific destination with its own parameters.
     */
    @Serializable
    data object Startup : TimetableNavDestination()

    /**
     * Represents a user-specific timetable destination.
     * This destination is used to navigate to the timetable for a specific user.
     */
    @Serializable
    data object UserTimetable : TimetableNavDestination()

    /**
     * Represents a study lines destination.
     * This destination is used to navigate to the list of study lines.
     */
    @Serializable
    data object StudyLines : TimetableNavDestination()

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
    ) : TimetableNavDestination()

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
    ) : TimetableNavDestination()

    /**
     * Represents a teachers destination.
     * This destination is used to navigate to the list of teachers.
     */
    @Serializable
    data object Teachers : TimetableNavDestination()

    /**
     * Represents a teacher timetable destination.
     * This destination is used to navigate to the timetable for a specific teacher.
     *
     * @property teacherId The identifier for the teacher.
     */
    @Serializable
    data class TeacherTimetable(val teacherId: String) : TimetableNavDestination()

    /**
     * Represents a subjects destination.
     * This destination is used to navigate to the list of subjects.
     */
    @Serializable
    data object Subjects : TimetableNavDestination()

    /**
     * Represents a subject timetable destination.
     * This destination is used to navigate to the timetable for a specific subject.
     *
     * @property subjectId The identifier for the subject.
     */
    @Serializable
    data class SubjectTimetable(val subjectId: String) : TimetableNavDestination()

    /**
     * Represents a rooms destination.
     * This destination is used to navigate to the list of rooms.
     */
    @Serializable
    data object Rooms : TimetableNavDestination()

    /**
     * Represents a room timetable destination.
     * This destination is used to navigate to the timetable for a specific room.
     *
     * @property roomId The identifier for the room.
     */
    @Serializable
    data class RoomTimetable(val roomId: String) : TimetableNavDestination()
}
