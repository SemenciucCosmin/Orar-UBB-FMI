package com.example.orarubb_fmi.common

import com.example.orarubb_fmi.data.datasource.dao.TimetableClassEntity
import com.example.orarubb_fmi.data.datasource.dao.TimetableInfoEntity
import com.example.orarubb_fmi.domain.model.ClassType
import com.example.orarubb_fmi.domain.model.Participant
import com.example.orarubb_fmi.domain.model.StudyField
import com.example.orarubb_fmi.domain.model.StudyLanguage
import com.example.orarubb_fmi.domain.model.Week
import com.example.orarubb_fmi.model.Timetable
import com.example.orarubb_fmi.model.TimetableClass
import com.example.orarubb_fmi.model.TimetableInfo
import java.util.UUID

val String.Companion.BLANK: String
    get() = ""

val String.Companion.SPACE: String
    get() = " "

val Timetable.id: String
    get() {
        val ids = info.year + info.semester + info.studyField.notation +
                info.studyLanguage.notation + info.studyYear
        return UUID.nameUUIDFromBytes(ids.toByteArray()).toString()
    }

val TimetableClass.id: String
    get() {
        val ids = group + day + startHour + endHour + week + place +
                participant + classType + discipline + professor
        return UUID.nameUUIDFromBytes(ids.toByteArray()).toString()
    }

fun Timetable.toTimetableInfoEntity(): TimetableInfoEntity {
    return TimetableInfoEntity(
        id = id,
        year = info.year,
        semester = info.semester,
        studyField = info.studyField.ordinal,
        studyLanguage = info.studyLanguage.ordinal,
        studyYear = info.studyYear
    )
}

fun TimetableClass.toTimetableClassEntity(): TimetableClassEntity {
    return TimetableClassEntity(
        id = id,
        group = group,
        day = day,
        startHour = startHour,
        endHour = endHour,
        week = week.ordinal,
        place = place,
        participant = participant.ordinal,
        classType = classType.ordinal,
        discipline = discipline,
        professor = professor
    )
}

fun TimetableInfoEntity.toTimetableInfo(): TimetableInfo {
    return TimetableInfo(
        year = year,
        semester = semester,
        studyField = StudyField.values()[studyField],
        studyLanguage = StudyLanguage.values()[studyLanguage],
        studyYear = studyYear
    )
}

fun TimetableClassEntity.toTimetableClass(): TimetableClass {
    return TimetableClass(
        group = group,
        day = day,
        startHour = startHour,
        endHour = endHour,
        week = Week.values()[week],
        place = place,
        participant = Participant.values()[participant],
        classType = ClassType.values()[classType],
        discipline = discipline,
        professor = professor
    )
}
