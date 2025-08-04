package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.teachers.api.TeachersApi
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherTimetable
import com.ubb.fmi.orar.data.teachers.model.TeacherTimetableClass
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status

class TeachersDataSourceImpl(
    private val teachersApi: TeachersApi
) : TeachersDataSource {

    override suspend fun getTeachers(year: Int, semesterId: String): Resource<List<Teacher>> {
        val resource = teachersApi.getTeachersHtml(
            year = year,
            semesterId = semesterId
        )

        val teachersHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val teachersTable = HtmlParser.extractTables(
            html = teachersHtml
        ).firstOrNull()

        val teachersCells = teachersTable?.rows?.map { it.cells }?.flatten()
        val teachers = teachersCells?.map { cell ->
            Teacher(
                id = cell.id,
                name = cell.value
            )
        }?.filter { it.name != NULL }

        return when {
            teachers.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(teachers, Status.Success)
        }
    }

    override suspend fun getTeacherTimetable(
        year: Int,
        semesterId: String,
        teacher: Teacher
    ): Resource<TeacherTimetable> {
        val resource = teachersApi.getTeacherTimetableHtml(
            year = year,
            semesterId = semesterId,
            teacherId = teacher.id
        )

        val teacherTimetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val teacherTable = HtmlParser.extractTables(
            html = teacherTimetableHtml
        ).firstOrNull()

        val classes = teacherTable?.rows?.mapNotNull { row ->
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val hoursCell = row.cells.getOrNull(HOURS_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null

            TeacherTimetableClass(
                day = dayCell.value,
                hours = hoursCell.value,
                frequencyId = frequencyCell.value,
                roomId = roomCell.id,
                studyLineId = studyLineCell.value,
                classTypeId = classTypeCell.value,
                subjectId = subjectCell.id,
                subjectName = subjectCell.value
            )
        }

        return when {
            classes.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(TeacherTimetable(teacher, classes), Status.Success)
        }
    }

    companion object {
        // Teacher timetable column indexes
        private const val DAY_INDEX = 0
        private const val HOURS_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val STUDY_LINE_INDEX = 5
        private const val CLASS_TYPE_INDEX = 6
        private const val SUBJECT_INDEX = 7

        private const val NULL = "null"
    }
}
