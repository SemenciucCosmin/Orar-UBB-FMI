package com.ubb.fmi.orar.data.studyline.datasource

import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.model.StudyLine
import com.ubb.fmi.orar.data.rooms.api.RoomsApi
import com.ubb.fmi.orar.data.rooms.model.Room
import com.ubb.fmi.orar.data.rooms.model.RoomTimetable
import com.ubb.fmi.orar.data.rooms.model.RoomTimetableClass
import com.ubb.fmi.orar.data.studyline.api.StudyLineApi
import com.ubb.fmi.orar.data.studyline.model.StudyLineGroupTimetable
import com.ubb.fmi.orar.data.studyline.model.StudyLineGroupTimetableClass
import com.ubb.fmi.orar.data.studyline.model.StudyLineTimetable
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.domain.htmlparser.model.Table
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status

class StudyLineDataSourceImpl(
    private val studyLineApi: StudyLineApi,
) : StudyLineDataSource {

    override suspend fun getStudyLineTimetable(
        year: Int,
        semester: Semester,
        studyLine: StudyLine
    ): Resource<StudyLineTimetable> {
        val resource = studyLineApi.getStudyLineTimetable(
            year = year,
            semester = semester.id,
            studyLineId = studyLine.id
        )

        val studyLineHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val studyLineTables = HtmlParser.extractTables(
            html = studyLineHtml
        )

        val studyLineGroupsTimetables = studyLineTables.map(::getStudyLineGroupTimetable)

        return when {
            studyLineGroupsTimetables.isEmpty() -> Resource(null, Status.Error)

            else -> {
                Resource(
                    payload = StudyLineTimetable(studyLine, studyLineGroupsTimetables),
                    status = Status.Success
                )
            }
        }
    }

    private fun getStudyLineGroupTimetable(studyLineTable: Table): StudyLineGroupTimetable {
        val classes = studyLineTable.rows.mapNotNull { row ->
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val hoursCell = row.cells.getOrNull(HOURS_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null

            StudyLineGroupTimetableClass(
                day = dayCell.value,
                hours = hoursCell.value,
                frequencyId = frequencyCell.value,
                roomId = roomCell.id,
                participantId = getParticipantId(participantCell.value),
                classTypeId = classTypeCell.value,
                subjectId = subjectCell.id,
                teacherId = teacherCell.id
            )
        }

        return StudyLineGroupTimetable(
            groupId = studyLineTable.title,
            classes = classes
        )
    }

    private fun getParticipantId(participant: String): String {
        return when {
            participant.contains(SEMIGROUP_1_ID) -> SEMIGROUP_1_ID
            participant.contains(SEMIGROUP_2_ID) -> SEMIGROUP_2_ID
            participant.all { it.isDigit() } -> WHOLE_GROUP_ID
            else -> WHOLE_YEAR_ID
        }
    }

    companion object {
        // StudyLine timetable column indexes
        private const val DAY_INDEX = 0
        private const val HOURS_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val PARTICIPANT_INDEX = 4
        private const val CLASS_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7

        // ParticipantIds
        private const val SEMIGROUP_1_ID = "/1"
        private const val SEMIGROUP_2_ID = "/2"
        private const val WHOLE_GROUP_ID = "whole_group"
        private const val WHOLE_YEAR_ID = "whole_year"
    }
}
