package com.ubb.fmi.orar.data.calendar.datasource

import Logger
import com.ubb.fmi.orar.data.calendar.model.AcademicActivity
import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.calendar.model.AcademicPeriod
import com.ubb.fmi.orar.data.calendar.model.AcademicSemester
import com.ubb.fmi.orar.data.calendar.model.AcademicWeek
import com.ubb.fmi.orar.data.database.dao.AcademicPeriodDao
import com.ubb.fmi.orar.data.database.model.AcademicPeriodEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.AcademicCalendarApi
import com.ubb.fmi.orar.data.timetable.model.Semester
import com.ubb.fmi.orar.data.timetable.model.StudyLanguage
import com.ubb.fmi.orar.data.timetable.model.Week
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.DOT
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.extensions.substringBetween
import com.ubb.fmi.orar.domain.htmlparser.HtmlTableParser
import com.ubb.fmi.orar.domain.htmlparser.model.Row
import com.ubb.fmi.orar.domain.htmlparser.model.Table
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Instant

/**
 * Data source for managing academic calendar related information
 */
class AcademicCalendarDataSourceImpl(
    private val academicCalendarApi: AcademicCalendarApi,
    private val academicPeriodDao: AcademicPeriodDao,
    private val logger: Logger,
) : AcademicCalendarDataSource {

    /**
     * Retrieves [Flow] of academic calendar from database
     */
    override suspend fun getCalendarFromCache(year: Int): Flow<AcademicCalendar?> {
        return academicPeriodDao.getAllAsFlow(year).map { entities ->
            mapEntitiesToAcademicCalendar(entities)
        }
    }

    /**
     * Saves [academicCalendar] in database
     */
    override suspend fun saveCalendarInCache(academicCalendar: AcademicCalendar) {
        val entities = mapAcademicCalendarToEntities(academicCalendar)
        academicPeriodDao.insertAll(entities)
    }

    /**
     * Retrieves the academic calendar page HTML from API
     */
    override suspend fun getCalendarFromApi(): Resource<AcademicCalendar> {
        val resource = academicCalendarApi.getCalendar()
        logger.d(TAG, "getCalendarFromApi status: ${resource.status}")

        val html = resource.payload
        val title = html?.substringBetween(TITLE_TAG_OPEN, TITLE_TAG_CLOSED)
        val yearPeriod = title?.filter { it.isDigit() } ?: String.BLANK
        val startYear = yearPeriod.take(yearPeriod.length / 2).toIntOrNull()
        val endYear = yearPeriod.takeLast(yearPeriod.length / 2).toIntOrNull()

        if (startYear == null || endYear == null) return Resource(null, Status.Error)

        val tables = html?.let(HtmlTableParser::extractTables)
        val reTable = tables?.getOrNull(RE_TABLE_INDEX)
        val hgTable = tables?.getOrNull(HG_TABLE_INDEX)

        if (reTable == null || hgTable == null) return Resource(null, Status.Error)

        val reSemesters = getSemesters(
            table = reTable,
            studyLanguages = listOf(StudyLanguage.ROMANIAN, StudyLanguage.ENGLISH)
        )

        val hgSemesters = getSemesters(
            table = hgTable,
            studyLanguages = listOf(StudyLanguage.HUNGARIAN, StudyLanguage.GERMAN)
        )

        val academicCalendar = AcademicCalendar(
            startYear = startYear,
            endYear = endYear,
            semesters = reSemesters + hgSemesters
        )

        return Resource(
            payload = academicCalendar,
            status = Status.Success
        )
    }

    /**
     * Invalidates the cached academic calendar of [year]
     */
    override suspend fun invalidate(year: Int) {
        academicPeriodDao.deleteAll(year)
    }

    /**
     * Retrieves the semesters of a calendar [table] for [studyLanguages]
     */
    private fun getSemesters(
        table: Table,
        studyLanguages: List<StudyLanguage>,
    ): List<AcademicSemester> {
        val rows = table.rows.filter { row -> getActivity(row) != null }
        val semester1Rows = rows.take(SEMESTER_1_PERIODS)
        val semester2Rows = rows.drop(SEMESTER_1_PERIODS).take(SEMESTER_2_PERIODS)
        val semester2FinalRows = rows.takeLast(SEMESTER_2_FINAL_PERIODS)

        return listOf(
            AcademicSemester(
                semester = Semester.FIRST,
                isFinal = false,
                studyLanguages = studyLanguages,
                periods = getPeriods(semester1Rows),
            ),
            AcademicSemester(
                semester = Semester.SECOND,
                isFinal = false,
                studyLanguages = studyLanguages,
                periods = getPeriods(semester2Rows),
            ),
            AcademicSemester(
                semester = Semester.SECOND,
                isFinal = true,
                studyLanguages = studyLanguages,
                periods = getPeriods(semester2FinalRows),
            ),
        )
    }

    /**
     * Retrieves the periods of a semester from its table [rows]
     */
    private fun getPeriods(rows: List<Row>): List<AcademicPeriod> {
        val periods = mutableListOf<AcademicPeriod>()

        rows.forEach { row ->
            val activity = getActivity(row) ?: return@forEach
            val interval = row.cells.getOrNull(INTERVAL_CELL_INDEX)?.value
                ?.replace(String.SPACE, String.BLANK)
                ?.replace(DASH_CODE, String.DASH)
                ?: return@forEach

            val startDateMillis = getMillisFromDate(interval.substringBefore(String.DASH))
            val endDateMillis = getMillisFromDate(interval.substringAfter(String.DASH))

            if (startDateMillis == null || endDateMillis == null) return@forEach

            val period = AcademicPeriod(
                activity = activity,
                weeks = getWeeks(activity, startDateMillis, endDateMillis, periods),
                startDateMillis = startDateMillis,
                endDateMillis = endDateMillis,
            )

            periods.add(period)
        }

        return periods
    }

    /**
     * Retrieves the activity of a table [row]
     */
    private fun getActivity(row: Row): AcademicActivity? {
        return row.cells.getOrNull(ACTIVITY_CELL_INDEX)?.value?.let(AcademicActivity::getById)
    }

    /**
     * Retrieves the millis at the start of a [date] formatted as dd.MM.yyyy
     */
    private fun getMillisFromDate(date: String): Long? {
        val (day, month, year) = date.split(String.DOT)

        val localDate = try {
            LocalDate.parse("$year${String.DASH}$month${String.DASH}$day")
        } catch (_: Exception) {
            return null
        }

        return localDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    /**
     * Retrieves the Monday to Monday weeks of a period. Teaching weeks alternate between odd and
     * even, continuing from the teaching weeks of [previousPeriods] so a break doesn't shift them.
     * Any other week is odd.
     */
    private fun getWeeks(
        activity: AcademicActivity,
        startDateMillis: Long,
        endDateMillis: Long,
        previousPeriods: List<AcademicPeriod>,
    ): List<AcademicWeek> {
        val timeZone = TimeZone.currentSystemDefault()
        val startDate = Instant.fromEpochMilliseconds(startDateMillis).toLocalDateTime(timeZone).date
        val endDate = Instant.fromEpochMilliseconds(endDateMillis).toLocalDateTime(timeZone).date
        val firstMonday = startDate.minus(startDate.dayOfWeek.ordinal, DateTimeUnit.DAY)
        val weeksCount = firstMonday.daysUntil(endDate) / DAYS_IN_WEEK + 1

        val previousTeachingWeeksCount = previousPeriods
            .filter { it.activity == AcademicActivity.TEACHING_ACTIVITY }
            .sumOf { it.weeks.size }

        return List(weeksCount) { index ->
            val monday = firstMonday.plus(index, DateTimeUnit.WEEK)
            val nextMonday = monday.plus(1, DateTimeUnit.WEEK)
            val teachingWeekIndex = previousTeachingWeeksCount + index

            val week = when {
                activity != AcademicActivity.TEACHING_ACTIVITY -> Week.ODD
                teachingWeekIndex % 2 == 0 -> Week.ODD
                else -> Week.EVEN
            }

            AcademicWeek(
                week = week,
                startMillis = monday.atStartOfDayIn(timeZone).toEpochMilliseconds(),
                endMillis = nextMonday.atStartOfDayIn(timeZone).toEpochMilliseconds(),
            )
        }
    }

    /**
     * Maps an [AcademicCalendar] into a list of [AcademicPeriodEntity]
     */
    private fun mapAcademicCalendarToEntities(
        academicCalendar: AcademicCalendar,
    ): List<AcademicPeriodEntity> {
        return academicCalendar.semesters.flatMap { semester ->
            val studyLanguageIds = semester.studyLanguages.joinToString(String.COMMA) { it.name }

            semester.periods.map { period ->
                AcademicPeriodEntity(
                    startYear = academicCalendar.startYear,
                    endYear = academicCalendar.endYear,
                    semesterId = semester.semester.id,
                    isFinal = semester.isFinal,
                    studyLanguageIds = studyLanguageIds,
                    activityId = period.activity.id,
                    startDateMillis = period.startDateMillis,
                    endDateMillis = period.endDateMillis,
                )
            }
        }
    }

    /**
     * Maps a list of [AcademicPeriodEntity] into an [AcademicCalendar]
     */
    private fun mapEntitiesToAcademicCalendar(
        entities: List<AcademicPeriodEntity>,
    ): AcademicCalendar? {
        val semesters = entities.groupBy { entity ->
            Triple(entity.semesterId, entity.isFinal, entity.studyLanguageIds)
        }.map { (key, semesterEntities) ->
            val (semesterId, isFinal, studyLanguageIds) = key

            AcademicSemester(
                semester = Semester.getById(semesterId),
                isFinal = isFinal,
                periods = mapEntitiesToPeriods(semesterEntities),
                studyLanguages = studyLanguageIds.split(String.COMMA).mapNotNull { id ->
                    StudyLanguage.entries.find { it.name == id }
                },
            )
        }

        return AcademicCalendar(
            startYear = entities.firstOrNull()?.startYear ?: return null,
            endYear = entities.firstOrNull()?.endYear ?: return null,
            semesters = semesters,
        )
    }

    /**
     * Maps the [entities] of a semester into a list of [AcademicPeriod]
     */
    private fun mapEntitiesToPeriods(entities: List<AcademicPeriodEntity>): List<AcademicPeriod> {
        val periods = mutableListOf<AcademicPeriod>()

        entities.forEach { entity ->
            val activity = AcademicActivity.getById(entity.activityId) ?: return@forEach

            val period = AcademicPeriod(
                activity = activity,
                weeks = getWeeks(activity, entity.startDateMillis, entity.endDateMillis, periods),
                startDateMillis = entity.startDateMillis,
                endDateMillis = entity.endDateMillis,
            )

            periods.add(period)
        }

        return periods
    }

    companion object {
        private const val TAG = "CalendarDataSource"
        private const val TITLE_TAG_OPEN = "<title>"
        private const val TITLE_TAG_CLOSED = "</title>"
        private const val RE_TABLE_INDEX = 0 // Romanian and English
        private const val HG_TABLE_INDEX = 1 // Hungarian and German
        private const val SEMESTER_1_PERIODS = 7
        private const val SEMESTER_2_PERIODS = 8
        private const val SEMESTER_2_FINAL_PERIODS = 7
        private const val INTERVAL_CELL_INDEX = 0
        private const val ACTIVITY_CELL_INDEX = 1
        private const val DAYS_IN_WEEK = 7
        private const val DASH_CODE = "&#8211;"
    }
}
