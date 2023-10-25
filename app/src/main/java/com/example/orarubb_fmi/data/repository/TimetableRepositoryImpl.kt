package com.example.orarubb_fmi.data.repository

import com.example.orarubb_fmi.common.END_HOUR
import com.example.orarubb_fmi.common.HEADLINE_TAG
import com.example.orarubb_fmi.common.HOURS_DELIMITER
import com.example.orarubb_fmi.common.HOUR_FORMAT
import com.example.orarubb_fmi.common.SPACE
import com.example.orarubb_fmi.common.START_HOUR
import com.example.orarubb_fmi.common.TABLE_COLUMN_TAG
import com.example.orarubb_fmi.common.TABLE_ROW_TAG
import com.example.orarubb_fmi.common.TABLE_TAG
import com.example.orarubb_fmi.common.toTimetableClass
import com.example.orarubb_fmi.common.toTimetableClassEntity
import com.example.orarubb_fmi.common.toTimetableInfo
import com.example.orarubb_fmi.common.toTimetableInfoEntity
import com.example.orarubb_fmi.data.datasource.api.TimetableApiService
import com.example.orarubb_fmi.data.datasource.dao.TimetableDao
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
    private val timetableApiService: TimetableApiService,
    private val timetableDao: TimetableDao
) : TimetableRepository {

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

    override suspend fun getCachedTimetable(): Timetable {
        val timetableInfoEntity = timetableDao.getTimetableInfo()
        val timetableClassEntities = timetableDao.getTimetableClasses()
        return Timetable(
            info = timetableInfoEntity.toTimetableInfo(),
            classes = timetableClassEntities.map { it.toTimetableClass() }
        )
    }

    override suspend fun saveTimetable(timetable: Timetable) {
        timetableDao.insertTimetableInfo(timetable.toTimetableInfoEntity())
        timetable.classes.forEach { timetableClass ->
            timetableDao.insertTimetableClass(timetableClass.toTimetableClassEntity())
        }
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
