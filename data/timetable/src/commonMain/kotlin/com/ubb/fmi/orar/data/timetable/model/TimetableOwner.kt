package com.ubb.fmi.orar.data.timetable.model

/**
 * Class for any [TimetableOwner]
 * @param [id]: unique identifier
 * @param [name]: name of the owner
 * @param [configurationId]: configuration id to which this owner belongs to
 * @param [type]: owner type (Room, Study Line, Subject, Teacher)
 */
sealed class TimetableOwner(
    open val id: String,
    open val name: String,
    open val configurationId: String,
    open val type: TimetableOwnerType
) {

    /**
     * Room [TimetableOwner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     * @param [type]: owner type (Room, Study Line, Subject, Teacher)
     * @param [location]: location of the room
     */
    data class Room(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val location: String
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.ROOM)

    /**
     * StudyLine [TimetableOwner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     * @param [type]: owner type (Room, Study Line, Subject, Teacher)
     * @param [fieldId]: study line field
     * @param [levelId]: study line level (Year1, Year2, Year3)
     * @param [degreeId]: study line degree (License or Master)
     */
    data class StudyLine(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val fieldId: String,
        val levelId: String,
        val degreeId: String,
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.STUDY_LINE)

    /**
     * Subject [TimetableOwner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     * @param [type]: owner type (Room, Study Line, Subject, Teacher)
     */
    data class Subject(
        override val id: String,
        override val name: String,
        override val configurationId: String,
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.SUBJECT)

    /**
     * Teacher [TimetableOwner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     * @param [type]: owner type (Room, Study Line, Subject, Teacher)
     * @param [titleId]: teacher title
     */
    data class Teacher(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val titleId: String,
    ) : TimetableOwner(id, name, configurationId, TimetableOwnerType.TEACHER)
}
