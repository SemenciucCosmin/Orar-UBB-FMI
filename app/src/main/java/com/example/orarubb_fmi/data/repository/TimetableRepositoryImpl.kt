package com.example.orarubb_fmi.data.repository

import com.example.orarubb_fmi.common.SPACE
import com.example.orarubb_fmi.data.datasource.api.TimetableApiService
import com.example.orarubb_fmi.domain.model.ClassType
import com.example.orarubb_fmi.domain.model.Participant
import com.example.orarubb_fmi.model.Timetable
import com.example.orarubb_fmi.model.TimetableClass
import com.example.orarubb_fmi.model.TimetableInfo
import com.example.orarubb_fmi.domain.model.Week
import com.example.orarubb_fmi.domain.repository.TimetableRepository
import org.jsoup.Jsoup
import org.jsoup.select.Elements

class TimetableRepositoryImpl(
    private val timetableApiService: TimetableApiService
) : TimetableRepository {

    companion object {
        private const val HEADLINE_TAG = "h1"
        private const val TABLE_TAG = "table"
        private const val TABLE_ROW_TAG = "tr"
        private const val TABLE_COLUMN_TAG = "td"
        private const val HOURS_DELIMITER = "-"
        private const val START_HOUR = 0
        private const val END_HOUR = 1
        private const val HOUR_FORMAT = "%02d:00"
    }

    override suspend fun getGroups(timetableInfo: TimetableInfo): List<String> {
        val htmlResponse = timetableApiService.getTimetablesHtml(
            year = timetableInfo.year,
            semester = timetableInfo.semester,
            studyField = timetableInfo.studyField.notation,
            studyLanguage = timetableInfo.studyLanguage.notation,
            studyYear = timetableInfo.studyYear
        )
        val document = Jsoup.parse(htmlResponse.string())
        val headlineTags = document.select(HEADLINE_TAG)
        val groupTags = headlineTags.subList(1, headlineTags.size)
        val groups = groupTags.mapNotNull { tag ->
            tag.text().split(String.SPACE).lastOrNull()
        }
        return groups
    }

    override suspend fun getTimetable(timetableInfo: TimetableInfo): Timetable {
        val htmlResponse = timetableApiService.getTimetablesHtml(
            year = timetableInfo.year,
            semester = timetableInfo.semester,
            studyField = timetableInfo.studyField.notation,
            studyLanguage = timetableInfo.studyLanguage.notation,
            studyYear = timetableInfo.studyYear
        )
        val document = Jsoup.parse(htmlResponse.string())
        val tables = document.select(TABLE_TAG)
        val headlineTags = document.select(HEADLINE_TAG)
        val groupElements = headlineTags.subList(1, headlineTags.size)
        val groups = groupElements.mapNotNull { element ->
            element.text().split(String.SPACE).lastOrNull()
        }

        val timetablesClasses = tables.mapIndexed { index, table ->
            val rows = table.select(TABLE_ROW_TAG)
            rows.mapNotNull { row ->
                getTimetableClass(
                    group = groups[index],
                    columns = row.select(TABLE_COLUMN_TAG)
                )
            }
        }.flatten()

        return Timetable(
            info = timetableInfo,
            classes = timetablesClasses
        )
    }

    override suspend fun getCachedTimetable(): Timetable? {
        TODO("Not yet implemented")
    }

    override suspend fun saveTimetable(timetable: Timetable) {
        TODO("Not yet implemented")
    }

    private fun getTimetableClass(group: String, columns: Elements): TimetableClass? {
        if (columns.isEmpty()) return null

        val dayElement = columns[0].text() ?: return null
        val hourIntervalElement = columns[1].text() ?: return null
        val weekElement = columns[2].text() ?: return null
        val placeElement = columns[3].text() ?: return null
        val participantElement = columns[4].text() ?: return null
        val classTypeElement = columns[5].text() ?: return null
        val disciplineElement = columns[6].text() ?: return null
        val professorElement = columns[7].text() ?: return null

        val hours = hourIntervalElement.split(HOURS_DELIMITER)
        val startHour = String.format(HOUR_FORMAT, hours[START_HOUR].toInt())
        val endHour = String.format(HOUR_FORMAT, hours[END_HOUR].toInt())

        val week = Week.getWeekType(weekElement)
        val participant = Participant.getParticipantType(participantElement)
        val classType = ClassType.getClassType(classTypeElement)

        return TimetableClass(
            group = group,
            day = dayElement,
            startHour = startHour,
            endHour = endHour,
            week = week,
            place = placeElement,
            participant = participant,
            classType = classType,
            discipline = disciplineElement,
            professor = professorElement
        )
    }
}
