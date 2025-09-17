package com.ubb.fmi.orar.data.studylines.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.StudyLinesApi
import com.ubb.fmi.orar.data.studylines.model.Degree
import com.ubb.fmi.orar.data.timetable.datasource.TimetableDataSource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.extensions.SLASH
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.domain.htmlparser.model.Table
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing study line related information
 */
class StudyLinesDataSourceImpl(
    private val studyLinesApi: StudyLinesApi,
    private val studyLineDao: StudyLineDao,
    private val logger: Logger,
    timetableClassDao: TimetableClassDao,
) : StudyLinesDataSource, TimetableDataSource<TimetableOwner.StudyLine>(
    timetableClassDao = timetableClassDao,
    logger = logger,
    tag = TAG,
) {

    /**
     * Retrieve list of [TimetableOwner.StudyLine] objects from cache or API
     * by [year] and [semesterId]
     */
    override suspend fun getOwners(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.StudyLine>> {
        return super.getOwners(year, semesterId)
    }

    /**
     * Retrieve list of groups of a [TimetableOwner.StudyLine] from cache or API
     * by [year], [semesterId] and [ownerId]
     */
    override suspend fun getGroups(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<List<String>> {
        logger.d(TAG, "getGroups for year: $year, semester: $semesterId, ownerId: $ownerId")
        val resource = super.getTimetable(year, semesterId, ownerId)

        logger.d(TAG, "getGroups resource: $resource")
        val groups = resource.payload?.classes?.map { it.groupId }?.distinct()?.sortedBy { it }

        logger.d(TAG, "getGroups groups: $groups")
        return Resource(groups, resource.status)
    }

    /**
     * Retrieve timetable of [TimetableOwner.StudyLine] for specific study line from cache or
     * API by [year], [semesterId], [ownerId] and [groupId]
     */
    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
        groupId: String,
    ): Resource<Timetable<TimetableOwner.StudyLine>> {
        logger.d(TAG, "getTimetable for year: $year, semester: $semesterId")
        logger.d(TAG, "getTimetable for ownerId: $ownerId, groupId: $groupId")

        val resource = super.getTimetable(year, semesterId, ownerId)
        logger.d(TAG, "getTimetable resource: $resource")

        val filteredClasses = resource.payload?.classes?.filter { it.groupId == groupId }
        logger.d(TAG, "getTimetable filteredClasses: $filteredClasses")

        val timetable = filteredClasses?.let { resource.payload?.copy(classes = filteredClasses) }
        logger.d(TAG, "getTimetable timetable: $timetable")

        return Resource(timetable, resource.status)
    }

    /**
     * Change visibility of specific study line timetable class by [timetableClassId]
     */
    override suspend fun changeTimetableClassVisibility(
        timetableClassId: String,
    ) {
        super.changeTimetableClassVisibility(timetableClassId)
    }

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        super.invalidate(year, semesterId)
    }

    /**
     * Retrieve list of [TimetableOwner.StudyLine] objects from cache by [configurationId]
     */
    override suspend fun getOwnersFromCache(
        configurationId: String,
    ): List<TimetableOwner.StudyLine> {
        val entities = studyLineDao.getAll(configurationId)
        return entities.map(::mapEntityToOwner)
    }

    /**
     * Saves new study line [owner] to cache
     */
    override suspend fun saveOwnerInCache(owner: TimetableOwner.StudyLine) {
        val entity = mapOwnerToEntity(owner)
        studyLineDao.insert(entity)
    }

    /**
     * Retrieve list of [TimetableOwner.StudyLine] objects from API by [year] and [semesterId]
     */
    override suspend fun getOwnersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.StudyLine>> {
        logger.d(TAG, "getOwnersFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = studyLinesApi.getOwnersHtml(year, semesterId)

        logger.d(TAG, "getOwnersFromApi resource: $resource")

        val ownersHtml = resource.payload
        val tables = ownersHtml?.let(HtmlParser::extractTables)

        logger.d(TAG, "getOwnersFromApi tables: $tables")

        val owners = tables?.mapIndexed { tableIndex, table ->
            logger.d(TAG, "getOwnersFromApi table: $table")
            table.rows.mapNotNull { row ->
                logger.d(TAG, "getOwnersFromApi row: $row")
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

                    val degreeId = when {
                        tableIndex == MASTER_DEGREE_TABLE_INDEX -> Degree.MASTER.id
                        else -> Degree.LICENCE.id
                    }

                    TimetableOwner.StudyLine(
                        id = lineId,
                        name = nameCell.value,
                        fieldId = fieldId,
                        levelId = levelCell.value,
                        degreeId = degreeId,
                        configurationId = configurationId,
                    )
                }
            }.flatten()
        }?.flatten() ?: emptyList()

        logger.d(TAG, "getOwnersFromApi owners: $owners")

        return when {
            owners.isEmpty() -> Resource(null, Status.Error)
            else -> Resource(owners, Status.Success)
        }
    }

    /**
     * Retrieve timetable of [TimetableOwner.StudyLine] for specific room from API
     * by [year], [semesterId] and [owner]
     */
    override suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        owner: TimetableOwner.StudyLine,
    ): Resource<Timetable<TimetableOwner.StudyLine>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId, owner: $owner")

        val configurationId = year.toString() + semesterId
        val resource = studyLinesApi.getTimetableHtml(year, semesterId, owner.id)

        logger.d(TAG, "getTimetableFromApi resource: $resource")

        val timetableHtml = resource.payload
        val tables = timetableHtml?.let(HtmlParser::extractTables)

        logger.d(TAG, "getTimetableFromApi tables: $tables")

        val joinedTables = tables?.map { table ->
            val groupTitle = table.title.substringBefore(String.SLASH)
            val groupId = groupTitle.substringAfter(String.SPACE)
            table.copy(title = groupId)
        }?.groupBy { it.title }?.map { (groupId, tables) ->
            Table(
                title = groupId,
                rows = tables.flatMap { it.rows }.distinct()
            )
        }

        logger.d(TAG, "getTimetableFromApi joinedTables: $joinedTables")
        val rowsCount = joinedTables?.sumOf { table -> table.rows.size }
        val classes = joinedTables?.map { table ->
            logger.d(TAG, "getTimetableFromApi table: $table")
            val groupId = table.title

            table.rows.mapNotNull { row ->
                logger.d(TAG, "getTimetableFromApi row: $row")
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

                val id = listOf(
                    dayCell.value,
                    intervalCell.value,
                    frequencyCell.value,
                    roomCell.id,
                    owner.name,
                    participantCell.value,
                    classTypeCell.value,
                    groupId,
                    subjectCell.id,
                    teacherCell.id,
                ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

                TimetableClass(
                    id = id,
                    day = dayCell.value,
                    startHour = startHour,
                    endHour = endHour,
                    frequencyId = frequencyCell.value,
                    room = roomCell.value,
                    field = owner.name,
                    participant = participantCell.value,
                    classType = classTypeCell.value,
                    ownerTypeId = owner.type.id,
                    ownerId = owner.id,
                    groupId = groupId,
                    subject = subjectCell.value,
                    teacher = teacherCell.value,
                    isVisible = true,
                    configurationId = configurationId
                )
            }
        }?.flatten() ?: emptyList()

        logger.d(TAG, "getTimetableFromApi classes: $classes")

        return when {
            classes.size != rowsCount -> Resource(null, Status.Error)
            else -> Resource(Timetable(owner, classes), Status.Success)
        }
    }

    /**
     * Sorts rooms by name
     */
    override fun sortOwners(
        owners: List<TimetableOwner.StudyLine>,
    ): List<TimetableOwner.StudyLine> {
        return owners.sortedBy { it.name }
    }

    /**
     * Maps a [TimetableOwner.StudyLine] to a [StudyLineEntity]
     */
    private fun mapOwnerToEntity(owner: TimetableOwner.StudyLine): StudyLineEntity {
        return StudyLineEntity(
            id = owner.id,
            name = owner.name,
            configurationId = owner.configurationId,
            fieldId = owner.fieldId,
            levelId = owner.levelId,
            degreeId = owner.degreeId
        )
    }

    /**
     * Maps a [StudyLineEntity] to a [TimetableOwner.StudyLine]
     */
    private fun mapEntityToOwner(entity: StudyLineEntity): TimetableOwner.StudyLine {
        return TimetableOwner.StudyLine(
            id = entity.id,
            name = entity.name,
            configurationId = entity.configurationId,
            fieldId = entity.fieldId,
            levelId = entity.levelId,
            degreeId = entity.degreeId,
        )
    }

    companion object {
        private const val TAG = "StudyLinesDataSource"

        // StudyLine column indexes
        private const val NAME_INDEX = 0
        private const val LEVEL_1_INDEX = 1
        private const val LEVEL_2_INDEX = 2
        private const val LEVEL_3_INDEX = 3

        // StudyLine timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val PARTICIPANT_INDEX = 4
        private const val CLASS_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7

        // Degree
        private const val MASTER_DEGREE_TABLE_INDEX = 1
        private const val NULL = "null"

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
