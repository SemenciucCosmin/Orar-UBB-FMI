package com.ubb.fmi.orar.data.timetable.model

/**
 * Class for any [Owner]
 * @param [id]: unique identifier
 * @param [name]: name of the owner
 * @param [configurationId]: configuration id to which this owner belongs to
 */
sealed class Owner(
    open val id: String,
    open val name: String,
    open val configurationId: String,
) {

    /**
     * StudyLine [Owner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     * @param [studyLine]: study line to which this group belongs
     */
    data class Group(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val studyLine: StudyLine,
    ) : Owner(id, name, configurationId)

    /**
     * Room [Owner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     * @param [address]: address of the room
     */
    data class Room(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val address: String
    ) : Owner(id, name, configurationId)

    /**
     * Subject [Owner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     */
    data class Subject(
        override val id: String,
        override val name: String,
        override val configurationId: String,
    ) : Owner(id, name, configurationId)

    /**
     * Teacher [Owner]
     * @param [id]: unique identifier
     * @param [name]: name of the owner
     * @param [configurationId]: configuration id to which this owner belongs to
     * @param [title]: teacher title
     */
    data class Teacher(
        override val id: String,
        override val name: String,
        override val configurationId: String,
        val title: TeacherTitle,
    ) : Owner(id, name, configurationId)
}
