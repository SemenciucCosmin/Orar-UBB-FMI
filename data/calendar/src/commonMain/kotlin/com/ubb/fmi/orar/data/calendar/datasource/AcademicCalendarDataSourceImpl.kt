package com.ubb.fmi.orar.data.calendar.datasource

import Logger
import com.ubb.fmi.orar.data.calendar.model.AcademicActivity
import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.calendar.model.AcademicPeriod
import com.ubb.fmi.orar.data.calendar.model.AcademicSemester
import com.ubb.fmi.orar.data.database.dao.AcademicPeriodDao
import com.ubb.fmi.orar.data.database.model.AcademicPeriodEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.AcademicCalendarApi
import com.ubb.fmi.orar.data.timetable.model.Semester
import com.ubb.fmi.orar.data.timetable.model.StudyLanguage
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COMMA
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.DOT
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.extensions.substringBetween
import com.ubb.fmi.orar.domain.htmlparser.HtmlTableParser
import com.ubb.fmi.orar.domain.htmlparser.model.Table
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn

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

        val rePeriods = getPeriods(reTable)
        val hgPeriods = getPeriods(hgTable)

        val reSemesters = getSemesters(
            periods = rePeriods,
            studyLanguages = listOf(StudyLanguage.ROMANIAN, StudyLanguage.ENGLISH)
        )

        val hgSemesters = getSemesters(
            periods = hgPeriods,
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

    override suspend fun invalidate(year: Int) {
        academicPeriodDao.deleteAll(year)
    }

    private fun getPeriods(table: Table): List<AcademicPeriod> {
        return table.rows.mapNotNull { row ->
            val intervalCell = row.cells.getOrNull(INTERVAL_CELL_INDEX)
            val activityCell = row.cells.getOrNull(ACTIVITY_CELL_INDEX)
            val weeksCell = row.cells.getOrNull(WEEKS_CELL_INDEX)

            val activity = activityCell?.value?.let(AcademicActivity::getById)
            val weeksCount = weeksCell?.value
                ?.dropWhile { !it.isDigit() }
                ?.substringBefore(String.SPACE)?.toIntOrNull()

            val interval = intervalCell?.value
                ?.replace(String.SPACE, String.BLANK)
                ?.replace(DASH_CODE, String.DASH)

            val startDateString = interval?.substringBefore(String.DASH) ?: return@mapNotNull null
            val endDateString = interval.substringAfter(String.DASH)

            val startDateMillis = getMillisFromDate(startDateString)
            val endDateMillis = getMillisFromDate(endDateString)

            AcademicPeriod(
                activity = activity ?: return@mapNotNull null,
                weeksCount = weeksCount ?: NO_WEEKS,
                startDateMillis = startDateMillis ?: return@mapNotNull null,
                endDateMillis = endDateMillis ?: return@mapNotNull null,
            )
        }
    }

    private fun getMillisFromDate(date: String): Long? {
        val (day, month, year) = date.split(String.DOT)

        val date = try {
            LocalDate.parse("$year${String.DASH}$month${String.DASH}$day")
        } catch (_: Exception) {
            return null
        }

        return date.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    private fun getSemesters(
        periods: List<AcademicPeriod>,
        studyLanguages: List<StudyLanguage>
    ): List<AcademicSemester> {
        return listOf(
            AcademicSemester(
                semester = Semester.FIRST,
                isFinal = false,
                studyLanguages = studyLanguages,
                periods = periods.take(SEMESTER_1_PERIODS)
            ),
            AcademicSemester(
                semester = Semester.SECOND,
                isFinal = false,
                studyLanguages = studyLanguages,
                periods = periods.subList(
                    SEMESTER_1_PERIODS,
                    SEMESTER_1_PERIODS + SEMESTER_2_PERIODS
                )
            ),
            AcademicSemester(
                semester = Semester.SECOND,
                isFinal = true,
                studyLanguages = studyLanguages,
                periods = periods.takeLast(SEMESTER_2_FINAL_PERIODS)
            )
        )
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
                    weeksCount = period.weeksCount,
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
                studyLanguages = studyLanguageIds.split(String.COMMA).mapNotNull { id ->
                    StudyLanguage.entries.find { it.name == id }
                },
                periods = semesterEntities.mapNotNull { entity ->
                    AcademicPeriod(
                        weeksCount = entity.weeksCount,
                        startDateMillis = entity.startDateMillis,
                        endDateMillis = entity.endDateMillis,
                        activity = AcademicActivity.getById(
                            entity.activityId
                        ) ?: return@mapNotNull null,
                    )
                }
            )
        }

        return AcademicCalendar(
            startYear = entities.firstOrNull()?.startYear ?: return null,
            endYear = entities.firstOrNull()?.endYear ?: return null,
            semesters = semesters,
        )
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
        private const val WEEKS_CELL_INDEX = 2
        private const val NO_WEEKS = 0
        private const val DASH_CODE = "&#8211;"
    }
}
