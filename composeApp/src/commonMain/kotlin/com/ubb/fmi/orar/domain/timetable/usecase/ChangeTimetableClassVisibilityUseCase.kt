package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.domain.timetable.model.TimetableOwnerType

class ChangeTimetableClassVisibilityUseCase(
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLinesDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource
) {

    suspend operator fun invoke(
        timetableClassId: String,
        timetableOwnerType: TimetableOwnerType,
    ) {
        when (timetableOwnerType) {
            TimetableOwnerType.ROOM -> {
                roomsDataSource.changeTimetableClassVisibility(
                    timetableClassId = timetableClassId
                )
            }

            TimetableOwnerType.STUDY_LINE -> {
                studyLineDataSource.changeTimetableClassVisibility(
                    timetableClassId = timetableClassId
                )
            }

            TimetableOwnerType.SUBJECT -> {
                subjectsDataSource.changeTimetableClassVisibility(
                    timetableClassId = timetableClassId
                )
            }

            TimetableOwnerType.TEACHER -> {
                teachersDataSource.changeTimetableClassVisibility(
                    timetableClassId = timetableClassId
                )
            }
        }
    }
}