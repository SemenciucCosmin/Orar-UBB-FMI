package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.model.Semester
import com.ubb.fmi.orar.data.subjects.api.SubjectsApi
import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.data.subjects.model.SubjectTimetable
import com.ubb.fmi.orar.data.subjects.model.SubjectTimetableClass
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status

class SubjectsDataSourceImpl(
    private val subjectsApi: SubjectsApi
) : SubjectsDataSource {

    override suspend fun getSubjects(year: Int, semester: Semester): Resource<List<Subject>> {
        val resource = subjectsApi.getSubjectsMapHtml(
            year = year,
            semesterId = semester.id
        )

        val subjectsMapHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val subjectsTable = HtmlParser.extractTables(
            html = subjectsMapHtml
        ).firstOrNull()

        val subjects = subjectsTable?.rows?.mapNotNull { row ->
            val idCell = row.cells.getOrNull(ID_INDEX) ?: return@mapNotNull null
            val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null

            Subject(
                id = idCell.value,
                name = nameCell.value,
            )
        }

        return when {
            subjects.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(subjects, Status.Success)
        }
    }

    override suspend fun getSubjectTimetable(
        year: Int,
        semester: Semester,
        subject: Subject
    ): Resource<SubjectTimetable> {
        val resource = subjectsApi.getSubjectTimetableHtml(
            year = year,
            semesterId = semester.id,
            subjectId = subject.id
        )

        val subjectTimetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val subjectTable = HtmlParser.extractTables(
            html = subjectTimetableHtml
        ).firstOrNull()

        val classes = subjectTable?.rows?.mapNotNull { row ->
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val hoursCell = row.cells.getOrNull(HOURS_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null

            SubjectTimetableClass(
                day = dayCell.value,
                hours = hoursCell.value,
                frequencyId = frequencyCell.value,
                roomId = roomCell.id,
                studyLineId = studyLineCell.value,
                classTypeId = classTypeCell.value,
                teacherId = teacherCell.id
            )
        }

        return when {
            classes.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(SubjectTimetable(subject, classes), Status.Success)
        }
    }

    companion object {
        // Subjects map column indexes
        private const val ID_INDEX = 0
        private const val NAME_INDEX = 1

        // Subject timetable column indexes
        private const val DAY_INDEX = 0
        private const val HOURS_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val STUDY_LINE_INDEX = 5
        private const val CLASS_TYPE_INDEX = 6
        private const val TEACHER_INDEX = 7
    }
}
