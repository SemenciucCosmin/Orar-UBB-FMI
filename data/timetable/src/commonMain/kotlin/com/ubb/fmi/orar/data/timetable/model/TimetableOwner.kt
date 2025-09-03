package com.ubb.fmi.orar.data.timetable.model

sealed class TimetableOwner(
    open val id: String,
    open val name: String,
    open val configurationId: String,
    open val type: TimetableOwnerType
) {
    data class Room(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val location: String
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.ROOM)

    data class StudyLine(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val fieldId: String,
        val levelId: String,
        val degreeId: String,
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.STUDY_LINE)

    data class Subject(
        override val id: String,
        override val name: String,
        override val configurationId: String,
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.SUBJECT)

    data class Teacher(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val titleId: String,
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.TEACHER)
}
