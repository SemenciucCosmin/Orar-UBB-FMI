package com.ubb.fmi.orar.data.students.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.StudentsApi
import com.ubb.fmi.orar.data.timetable.model.Degree
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser

/**
 * Data source for managing study line related information
 */
class StudyLinesDataSourceImpl(
    private val studentsApi: StudentsApi,
    private val studyLineDao: StudyLineDao,
    private val logger: Logger,
) : StudyLinesDataSource {

    /**
     * Retrieve list of [StudyLine] objects from cache or API
     * by [year] and [semesterId]
     */
    override suspend fun getStudyLines(
        year: Int,
        semesterId: String,
    ): Resource<List<StudyLine>> {
        logger.d(TAG, "getStudyLines for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val cachedStudyLines = getStudyLinesFromCache(configurationId)

        return when {
            cachedStudyLines.isNotEmpty() -> {
                val sortedStudyLines = sortStudyLines(cachedStudyLines)
                logger.d(TAG, "getStudyLines from cache: $sortedStudyLines")
                Resource(sortedStudyLines, Status.Success)
            }

            else -> {
                val studyLinesResource = getStudyLinesFromApi(year, semesterId)
                studyLinesResource.payload?.forEach { saveStudyLineInCache(it) }

                val sortedStudyLines = studyLinesResource.payload?.let(::sortStudyLines)
                logger.d(TAG, "getStudyLines from API: $sortedStudyLines, ${studyLinesResource.status}")

                Resource(sortedStudyLines, studyLinesResource.status)
            }
        }
    }


    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate studyLines for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        studyLineDao.deleteAll(configurationId)
    }

    /**
     * Retrieve list of [StudyLine] objects from cache by [configurationId]
     */
    private suspend fun getStudyLinesFromCache(
        configurationId: String,
    ): List<StudyLine> {
        val entities = studyLineDao.getAll(configurationId)
        return entities.map(::mapEntityToStudyLine)
    }

    /**
     * Saves new [StudyLine] to cache
     */
    private suspend fun saveStudyLineInCache(studyLine: StudyLine) {
        val entity = mapStudyLineToEntity(studyLine)
        studyLineDao.insert(entity)
    }

    /**
     * Retrieve list of [StudyLine] objects from API by [year] and [semesterId]
     */
    private suspend fun getStudyLinesFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<StudyLine>> {
        logger.d(TAG, "getStudyLinesFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = studentsApi.getStudyLines(year, semesterId)

        logger.d(TAG, "getStudyLinesFromApi resource: $resource")

        val studyLinesHtml = resource.payload
        val tables = studyLinesHtml?.let(HtmlParser::extractTables)

        logger.d(TAG, "getStudyLinesFromApi tables: $tables")

        val studyLines = tables?.mapIndexed { tableIndex, table ->
            logger.d(TAG, "getStudyLinesFromApi table: $table")
            table.rows.mapNotNull { row ->
                logger.d(TAG, "getStudyLinesFromApi row: $row")
                val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null
                val level1Cell = row.cells.getOrNull(LEVEL_1_INDEX)
                val level2Cell = row.cells.getOrNull(LEVEL_2_INDEX)
                val level3Cell = row.cells.getOrNull(LEVEL_3_INDEX)

                listOfNotNull(level1Cell, level2Cell, level3Cell).filter {
                    it.value != NULL
                }.map { levelCell ->
                    val lineId = levelCell.id
                    val fieldId = levelCell.id.toCharArray().filter { char ->
                        char.isLetter()
                    }.joinToString(separator = String.BLANK)

                    val degree = when {
                        tableIndex == MASTER_DEGREE_TABLE_INDEX -> Degree.MASTER
                        else -> Degree.LICENCE
                    }

                    StudyLine(
                        id = lineId,
                        name = nameCell.value,
                        fieldId = fieldId,
                        level = StudyLevel.getById(levelCell.value),
                        degree = degree,
                        configurationId = configurationId,
                    )
                }
            }.flatten()
        }?.flatten() ?: emptyList()

        logger.d(TAG, "getStudyLinesFromApi owners: $studyLines")

        return when {
            studyLines.isEmpty() -> Resource(null, resource.status)
            else -> Resource(studyLines, Status.Success)
        }
    }

    /**
     * Sorts study lines by name
     */
    private fun sortStudyLines(
        studyLines: List<StudyLine>,
    ): List<StudyLine> {
        return studyLines.sortedBy { it.name }
    }

    /**
     * Maps a [StudyLine] to a [StudyLineEntity]
     */
    private fun mapStudyLineToEntity(studyLine: StudyLine): StudyLineEntity {
        return StudyLineEntity(
            id = studyLine.id,
            name = studyLine.name,
            configurationId = studyLine.configurationId,
            fieldId = studyLine.fieldId,
            levelId = studyLine.level.id,
            degreeId = studyLine.degree.id
        )
    }

    /**
     * Maps a [StudyLineEntity] to a [StudyLine]
     */
    private fun mapEntityToStudyLine(entity: StudyLineEntity): StudyLine {
        return StudyLine(
            id = entity.id,
            name = entity.name,
            configurationId = entity.configurationId,
            fieldId = entity.fieldId,
            level = StudyLevel.getById(entity.levelId),
            degree = Degree.getById(entity.degreeId),
        )
    }

    companion object {
        private const val TAG = "StudyLinesDataSource"

        // StudyLine column indexes
        private const val NAME_INDEX = 0
        private const val LEVEL_1_INDEX = 1
        private const val LEVEL_2_INDEX = 2
        private const val LEVEL_3_INDEX = 3

        // Degree
        private const val MASTER_DEGREE_TABLE_INDEX = 1
        private const val NULL = "null"
    }
}
