package com.ubb.fmi.orar.data.studyline.datasource

import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.database.model.StudyLineClassEntity
import com.ubb.fmi.orar.data.studyline.api.StudyLineApi
import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.data.studyline.model.StudyLineClass
import com.ubb.fmi.orar.data.studyline.model.StudyLineTimetable
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
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
    private val studyLineDao: StudyLineDao
) : StudyLineDataSource {

    override suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<StudyLineTimetable>> {
        val cachedTimetables = getTimetablesFromCache()

        return when {
            cachedTimetables.isNotEmpty() -> {
                println("TESTMESSAGE StudyLine: from cache")
                Resource(cachedTimetables, Status.Success)
            }

            else -> {
                println("TESTMESSAGE StudyLine: from API")
                val apiTimetablesResource = getTimetablesFromApi(year, semesterId)
                apiTimetablesResource.payload?.forEach { timetable ->
                    val studyLineEntity = mapStudyLineToEntity(timetable.studyLine)
                    val classesEntities = mapClassesToEntities(
                        studyLineId = timetable.studyLine.id,
                        classes = timetable.classes,
                    )

                    studyLineDao.insertStudyLine(studyLineEntity)
                    studyLineDao.insertStudyLineClasses(classesEntities)
                }

                apiTimetablesResource
            }
        }
    }

    private suspend fun getTimetablesFromCache(): List<StudyLineTimetable> {
        val entities = studyLineDao.getAllStudyLinesWithClasses()
        return entities.map { (studyLineEntity, classesEntities) ->
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

        val rowsCount = studyLineTables.sumOf { table -> table.rows.size }
        val studyLineClasses = studyLineTables.map { studyLineTable ->
            studyLineTable.rows.mapNotNull { row ->
                val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
                val hoursCell = row.cells.getOrNull(HOURS_INDEX) ?: return@mapNotNull null
                val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
                val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
                val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
                val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
                val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
                val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null

                val participantId = when {
                    participantCell.value.contains(SEMIGROUP_1_ID) -> SEMIGROUP_1_ID
                    participantCell.value.contains(SEMIGROUP_2_ID) -> SEMIGROUP_2_ID
                    participantCell.value.all { it.isDigit() } -> WHOLE_GROUP_ID
                    else -> WHOLE_YEAR_ID
                }

                val id = listOf(
                    dayCell.value,
                    hoursCell.value,
                    frequencyCell.value,
                    roomCell.id,
                    classTypeCell.value,
                    subjectCell.id,
                    teacherCell.id,
                ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

                StudyLineClass(
                    id = id,
                    groupId = studyLineTable.title,
                    day = dayCell.value,
                    hours = hoursCell.value,
                    frequencyId = frequencyCell.value,
                    roomId = roomCell.id,
                    participantId = participantId,
                    classTypeId = classTypeCell.value,
                    subjectId = subjectCell.id,
                    teacherId = teacherCell.id
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

        val rooms = studyLinesTables.map { table ->
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
                    StudyLine(
                        name = nameCell.value,
                        id = cell.id,
                        studyYearId = cell.value
                    )
                }
            }.flatten()
        }.flatten()

        return when {
            rooms.isEmpty() -> Resource(null, Status.Error)
            else -> Resource(rooms, Status.Success)
        }
    }

    private fun mapEntityToStudyLine(entity: StudyLineEntity): StudyLine {
        return StudyLine(
            id = entity.id,
            name = entity.name,
            studyYearId = entity.studyYearId
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
                hours = classEntity.hours,
                frequencyId = classEntity.frequencyId,
                roomId = classEntity.roomId,
                participantId = classEntity.participantId,
                classTypeId = classEntity.classTypeId,
                subjectId = classEntity.subjectId,
                teacherId = classEntity.teacherId
            )
        }
    }

    private fun mapStudyLineToEntity(studyLine: StudyLine): StudyLineEntity {
        return StudyLineEntity(
            id = studyLine.id,
            name = studyLine.name,
            studyYearId = studyLine.studyYearId
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
                hours = groupClass.hours,
                frequencyId = groupClass.frequencyId,
                roomId = groupClass.roomId,
                participantId = groupClass.participantId,
                classTypeId = groupClass.classTypeId,
                subjectId = groupClass.subjectId,
                teacherId = groupClass.teacherId
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
        private const val NULL = "null"
    }
}
