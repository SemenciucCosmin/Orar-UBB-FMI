package com.ubb.fmi.orar.domain.timetable.usecase

import Logger
import com.ubb.fmi.orar.data.rooms.datasource.RoomsDataSource
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.subjects.datasource.SubjectsDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.model.TimetableOwnerType

/**
 * Use case for changing the visibility of a timetable class based on its owner type.
 * This use case interacts with various data sources to perform the visibility change.
 *
 * @property roomsDataSource The data source for room-related operations.
 * @property studyLineDataSource The data source for study line-related operations.
 * @property subjectsDataSource The data source for subject-related operations.
 * @property teachersDataSource The data source for teacher-related operations.
 */
class ChangeTimetableClassVisibilityUseCase(
    private val roomsDataSource: RoomsDataSource,
    private val studyLineDataSource: StudyLinesDataSource,
    private val subjectsDataSource: SubjectsDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val logger: Logger,
) {
    /**
     * Changes the visibility of a timetable class based on its ID and owner type.
     *
     * @param timetableClassId The ID of the timetable class to change visibility for.
     * @param timetableOwnerType The type of owner for the timetable class (e.g., ROOM, STUDY_LINE, SUBJECT, TEACHER).
     */
    suspend operator fun invoke(
        timetableClassId: String,
        timetableOwnerType: TimetableOwnerType,
    ) {
        logger.d(TAG, "class: $timetableOwnerType, owner:$timetableOwnerType")
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

    companion object {
        private const val TAG = "ChangeTimetableClassVisibilityUseCase"
    }
}
