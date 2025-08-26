package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.studyline.datasource.StudyLineDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.domain.timetable.model.ClassOwner

class ChangeTimetableClassVisibilityUseCase(
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLineDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource
) {

    suspend operator fun invoke(
        timetableClassId: String,
        timetableClassOwner: ClassOwner,
    ) {
        when (timetableClassOwner) {
            ClassOwner.ROOM -> {
                roomsDataSource.changeTimetableClassVisibility(timetableClassId)
            }

            ClassOwner.STUDY_LINE -> {
                studyLineDataSource.changeTimetableClassVisibility(timetableClassId)
            }

            ClassOwner.SUBJECT -> {
                subjectsDataSource.changeTimetableClassVisibility(timetableClassId)
            }

            ClassOwner.TEACHER -> {
                teachersDataSource.changeTimetableClassVisibility(timetableClassId)
            }
        }
    }
}