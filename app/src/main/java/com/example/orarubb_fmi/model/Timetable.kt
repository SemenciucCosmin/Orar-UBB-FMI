package com.example.orarubb_fmi.model

import java.util.UUID

data class Timetable(
    val info: TimetableInfo,
    val classes: List<TimetableClass>
) {
    val id: String
        get() {
            val ids = info.year + info.semester + info.studyField.notation +
                    info.studyLanguage.notation + info.studyYear
            return UUID.nameUUIDFromBytes(ids.toByteArray()).toString()
        }
}
