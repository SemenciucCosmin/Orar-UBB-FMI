package com.ubb.fmi.orar.data.rooms.model

data class RoomTimetable (
    val room: Room,
    val classes: List<RoomTimetableClass>
)