package com.ubb.fmi.orar.data.studyline.datasource

import com.ubb.fmi.orar.data.database.dao.StudyLineClassDao
import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.database.model.StudyLineClassEntity
import com.ubb.fmi.orar.data.studyline.api.StudyLineApi
import com.ubb.fmi.orar.data.studyline.model.Degree
import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.data.studyline.model.StudyLineClass
import com.ubb.fmi.orar.data.studyline.model.StudyLineTimetable
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.extensions.SLASH
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.domain.htmlparser.model.Table
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import com.ubb.fmi.orar.network.model.isError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.encodeUtf8

class StudyLineDataSourceImpl(
    private val studyLineApi: StudyLineApi,
    private val studyLineDao: StudyLineDao,
    private val studyLineClassDao: StudyLineClassDao,
) : StudyLineDataSource {

    override suspend fun getStudyLines(
        year: Int,
        semesterId: String
    ): Resource<List<StudyLine>> {
        val cachedStudyLines = getStudyLinesFromCache()

        return when {
            cachedStudyLines.isNotEmpty() -> {
                println("TESTMESSAGE StudyLine: from cache")
                val orderedStudyLines = cachedStudyLines.sortedBy { it.name }
                Resource(orderedStudyLines, Status.Success)
            }

            else -> {
                println("TESTMESSAGE StudyLine: from API")
                val apiStudyLinesResource = getStudyLinesFromApi(year, semesterId)
                apiStudyLinesResource.payload?.forEach { teacher ->
                    val studyLinesEntity = mapStudyLineToEntity(teacher)
                    studyLineDao.insertStudyLine(studyLinesEntity)
                }

                val orderedStudyLines = apiStudyLinesResource.payload?.sortedBy { it.name }
                Resource(orderedStudyLines, apiStudyLinesResource.status)
            }
        }
    }

    override suspend fun getStudyGroupsIds(
        year: Int,
        semesterId: String,
        studyLineId: String
    ): Resource<List<String>> {
        val timetableResource = getTimetable(year, semesterId, studyLineId)
        val groupIds = timetableResource.payload?.classes?.map { it.groupId }?.distinct()
        return Resource(groupIds, timetableResource.status)
    }

    override suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<StudyLineTimetable>> {
        val cachedTimetables = getTimetablesFromCache()

        return when {
            cachedTimetables.isNotEmpty() -> {
                println("TESTMESSAGE StudyLine Timetable: from cache")
                Resource(cachedTimetables, Status.Success)
            }

            else -> {
                println("TESTMESSAGE StudyLine Timetable: from API")
                val apiTimetablesResource = getTimetablesFromApi(year, semesterId)
                apiTimetablesResource.payload?.forEach { timetable ->
                    val studyLineEntity = mapStudyLineToEntity(timetable.studyLine)
                    val classesEntities = mapClassesToEntities(
                        studyLineId = timetable.studyLine.id,
                        classes = timetable.classes,
                    )

                    studyLineDao.insertStudyLine(studyLineEntity)
                    classesEntities.forEach { studyLineClassDao.insertStudyLineClass(it) }
                }

                apiTimetablesResource
            }
        }
    }

    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        studyLineId: String
    ): Resource<StudyLineTimetable> {
        val studyLine = getStudyLines(year, semesterId).payload?.firstOrNull { it.id == studyLineId }
        val cachedTimetable = studyLine?.let { getTimetableFromCache(studyLine) }

        return when {
            cachedTimetable?.classes?.isNotEmpty() == true -> {
                println("TESTMESSAGE StudyLine Timetable: from cache")
                Resource(cachedTimetable, Status.Success)
            }

            else -> {
                println("TESTMESSAGE StudyLine Timetable: from API")
                if (studyLine == null) return Resource(null, Status.Error)
                val apiTimetableResource = getStudyLineTimetableFromApi(year, semesterId, studyLine)
                apiTimetableResource.payload?.let { timetable ->
                    val studyLineEntity = mapStudyLineToEntity(timetable.studyLine)
                    val classesEntities = mapClassesToEntities(
                        studyLineId = timetable.studyLine.id,
                        classes = timetable.classes
                    )

                    studyLineDao.insertStudyLine(studyLineEntity)
                    classesEntities.forEach { studyLineClassDao.insertStudyLineClass(it) }
                }

                apiTimetableResource
            }
        }
    }

    override suspend fun changeTimetableClassVisibility(timetableClassId: String) {
        val studyLineClassEntity = studyLineClassDao.getStudyLineClass(timetableClassId)
        val newStudyLineClassEntity = studyLineClassEntity.copy(
            isVisible = !studyLineClassEntity.isVisible
        )

        studyLineClassDao.insertStudyLineClass(newStudyLineClassEntity)
    }

    private suspend fun getStudyLinesFromCache(): List<StudyLine> {
        val entities = studyLineDao.getAllStudyLines()
        return entities.map(::mapEntityToStudyLine)
    }

    private suspend fun getTimetableFromCache(studyLine: StudyLine): StudyLineTimetable {
        val studyLineClassEntities = studyLineClassDao.getStudyLineClasses(studyLine.id)
        return StudyLineTimetable(
            studyLine = studyLine,
            classes = mapEntitiesToClasses(studyLineClassEntities),
        )
    }

    private suspend fun getTimetablesFromCache(): List<StudyLineTimetable> {
        val studyLineEntities = studyLineDao.getAllStudyLines()
        val studyLineClassEntities = studyLineClassDao.getAllStudyLineClasses()
        val groupedStudyLineClassEntities = studyLineClassEntities.groupBy { it.studyLineId }
        val studyLineWithClassesEntities = studyLineEntities.associateWith { studyLineEntity ->
            groupedStudyLineClassEntities[studyLineEntity.id].orEmpty()
        }.filter { it.value.isNotEmpty() }

        return studyLineWithClassesEntities.map { (studyLineEntity, classesEntities) ->
            StudyLineTimetable(
                studyLine = mapEntityToStudyLine(studyLineEntity),
                classes = mapEntitiesToClasses(classesEntities),
            )
        }
    }

    private suspend fun getTimetablesFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<StudyLineTimetable>> {
        return withContext(Dispatchers.Default) {
            val studyLinesResource = getStudyLinesFromApi(year, semesterId)
            val studyLines = studyLinesResource.payload ?: emptyList()
            val studyLineTimetablesResources = studyLines.map { studyLine ->
                async { getStudyLineTimetableFromApi(year, semesterId, studyLine) }
            }.awaitAll()

            val studyLineTimetables = studyLineTimetablesResources.mapNotNull { it.payload }
            val errorStatus = studyLineTimetablesResources.map {
                it.status
            }.firstOrNull { it.isError() }

            return@withContext when {
                studyLinesResource.status.isError() -> Resource(null, studyLinesResource.status)
                errorStatus != null -> Resource(null, errorStatus)
                else -> Resource(studyLineTimetables, Status.Success)
            }
        }
    }

    private suspend fun getStudyLineTimetableFromApi(
        year: Int,
        semesterId: String,
        studyLine: StudyLine
    ): Resource<StudyLineTimetable> {
        val resource = studyLineApi.getStudyLineTimetable(
            year = year,
            semesterId = semesterId,
            studyLineId = studyLine.id
        )

        val studyLineHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val studyLineTables = HtmlParser.extractTables(studyLineHtml)
        val joinedStudyLineTables = studyLineTables.map { studyLineTable ->
            studyLineTable.copy(title = studyLineTable.title.substringBefore(String.SLASH))
        }.groupBy { it.title }.map { (groupId, tables) ->
            Table(
                title = groupId,
                rows = tables.flatMap { it.rows }.distinct()
            )
        }

        val rowsCount = joinedStudyLineTables.sumOf { table -> table.rows.size }
        val studyLineClasses = joinedStudyLineTables.map { studyLineTable ->
            studyLineTable.rows.mapNotNull { row ->
                val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
                val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
                val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
                val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
                val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
                val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
                val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null
                val intervals = intervalCell.value.split(String.DASH)
                val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
                val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null

                val participantCell = row.cells.getOrNull(
                    PARTICIPANT_INDEX
                ) ?: return@mapNotNull null

                val groupId = studyLineTable.title
                val participantSemigroupId = participantCell.value.apply {
                    replace(groupId, String.BLANK)
                    replace(String.SLASH, String.BLANK)
                }

                val participantId = when {
                    participantSemigroupId == SEMIGROUP_1 -> SEMIGROUP_1_ID
                    participantSemigroupId == SEMIGROUP_2 -> SEMIGROUP_2_ID
                    participantCell.value == groupId -> WHOLE_GROUP_ID
                    else -> WHOLE_YEAR_ID
                }

                val id = listOf(
                    dayCell.value,
                    intervalCell.value,
                    frequencyCell.value,
                    roomCell.id,
                    participantCell.value,
                    classTypeCell.value,
                    subjectCell.id,
                    teacherCell.id,
                ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

                StudyLineClass(
                    id = id,
                    groupId = groupId,
                    day = dayCell.value,
                    startHour = "$startHour:00",
                    endHour = "$endHour:00",
                    frequencyId = frequencyCell.value,
                    roomId = roomCell.id,
                    participantId = participantId,
                    participantName = participantCell.value,
                    classTypeId = classTypeCell.value,
                    subjectId = subjectCell.id,
                    teacherId = teacherCell.id,
                    isVisible = true
                )
            }
        }.flatten()

        return when {
            studyLineClasses.size != rowsCount -> Resource(null, Status.Error)

            else -> {
                Resource(
                    status = Status.Success,
                    payload = StudyLineTimetable(
                        studyLine = studyLine,
                        classes = studyLineClasses,
                    )
                )
            }
        }
    }

    private suspend fun getStudyLinesFromApi(
        year: Int,
        semesterId: String
    ): Resource<List<StudyLine>> {
        val resource = studyLineApi.getStudyLines(
            year = year,
            semesterId = semesterId
        )

        val studyLinesMapHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val studyLinesTables = HtmlParser.extractTables(
            html = studyLinesMapHtml
        )

        val studyLines = studyLinesTables.mapIndexed { tableIndex, table ->
            table.rows.mapNotNull { row ->
                val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null
                val studyYear1Cell = row.cells.getOrNull(STUDY_YEAR_1_INDEX)
                val studyYear2Cell = row.cells.getOrNull(STUDY_YEAR_2_INDEX)
                val studyYear3Cell = row.cells.getOrNull(STUDY_YEAR_3_INDEX)

                listOfNotNull(
                    studyYear1Cell,
                    studyYear2Cell,
                    studyYear3Cell,
                ).filter { it.value != NULL }.map { cell ->
                    val id = cell.id.toCharArray().filter { char ->
                        char.isLetter()
                    }.joinToString(separator = String.BLANK)

                    StudyLine(
                        name = nameCell.value,
                        id = cell.id,
                        baseId = id,
                        studyYearId = cell.value,
                        degreeId = when {
                            tableIndex == MASTER_DEGREE_TABLE_INDEX -> Degree.MASTER.id
                            else -> Degree.LICENCE.id
                        }
                    )
                }
            }.flatten()
        }.flatten()

        return when {
            studyLines.isEmpty() -> Resource(null, Status.Error)
            else -> Resource(studyLines, Status.Success)
        }
    }

    private fun mapEntityToStudyLine(entity: StudyLineEntity): StudyLine {
        return StudyLine(
            id = entity.id,
            name = entity.name,
            baseId = entity.lineId,
            studyYearId = entity.studyYearId,
            degreeId = entity.degreeId
        )
    }

    private fun mapEntitiesToClasses(
        entities: List<StudyLineClassEntity>
    ): List<StudyLineClass> {
        return entities.map { classEntity ->
            StudyLineClass(
                id = classEntity.id,
                groupId = classEntity.groupId,
                day = classEntity.day,
                startHour = classEntity.startHour,
                endHour = classEntity.endHour,
                frequencyId = classEntity.frequencyId,
                roomId = classEntity.roomId,
                participantId = classEntity.participantId,
                participantName = classEntity.participantName,
                classTypeId = classEntity.classTypeId,
                subjectId = classEntity.subjectId,
                teacherId = classEntity.teacherId,
                isVisible = classEntity.isVisible,
            )
        }
    }

    private fun mapStudyLineToEntity(studyLine: StudyLine): StudyLineEntity {
        return StudyLineEntity(
            id = studyLine.id,
            name = studyLine.name,
            lineId = studyLine.baseId,
            studyYearId = studyLine.studyYearId,
            degreeId = studyLine.degreeId
        )
    }

    private fun mapClassesToEntities(
        studyLineId: String,
        classes: List<StudyLineClass>
    ): List<StudyLineClassEntity> {
        return classes.map { groupClass ->
            StudyLineClassEntity(
                id = groupClass.id,
                studyLineId = studyLineId,
                groupId = groupClass.groupId,
                day = groupClass.day,
                startHour = groupClass.startHour,
                endHour = groupClass.endHour,
                frequencyId = groupClass.frequencyId,
                roomId = groupClass.roomId,
                participantId = groupClass.participantId,
                participantName = groupClass.participantName,
                classTypeId = groupClass.classTypeId,
                subjectId = groupClass.subjectId,
                teacherId = groupClass.teacherId,
                isVisible = groupClass.isVisible,
            )
        }
    }

    companion object {
        // StudyLine column indexes
        private const val NAME_INDEX = 0
        private const val STUDY_YEAR_1_INDEX = 1
        private const val STUDY_YEAR_2_INDEX = 2
        private const val STUDY_YEAR_3_INDEX = 3

        // StudyLine timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val PARTICIPANT_INDEX = 4
        private const val CLASS_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7

        // ParticipantIds
        private const val SEMIGROUP_1 = "1"
        private const val SEMIGROUP_2 = "2"
        private const val SEMIGROUP_1_ID = "/1"
        private const val SEMIGROUP_2_ID = "/2"
        private const val WHOLE_GROUP_ID = "whole_group"
        private const val WHOLE_YEAR_ID = "whole_year"
        private const val NULL = "null"

        // Degree
        private const val MASTER_DEGREE_TABLE_INDEX = 1

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
