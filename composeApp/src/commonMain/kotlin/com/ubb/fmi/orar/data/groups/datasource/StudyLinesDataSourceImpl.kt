package com.ubb.fmi.orar.data.groups.datasource

import com.ubb.fmi.orar.data.database.dao.StudyLineDao
import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.StudyLineEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.data.timetable.datasource.TimetableDataSource
import com.ubb.fmi.orar.data.groups.api.StudyLinesApi
import com.ubb.fmi.orar.data.groups.model.Degree
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.extensions.SLASH
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.domain.htmlparser.model.Table
import okio.ByteString.Companion.encodeUtf8

class StudyLinesDataSourceImpl(
    private val studyLinesApi: StudyLinesApi,
    private val studyLineDao: StudyLineDao,
    timetableClassDao: TimetableClassDao,
) : StudyLinesDataSource, TimetableDataSource<TimetableOwner.StudyLine>(timetableClassDao) {

    override suspend fun getOwners(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.StudyLine>> {
        return super.getOwners(year, semesterId)
    }

    override suspend fun getGroups(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<List<String>> {
        val resource = super.getTimetable(year, semesterId, ownerId)
        val groups = resource.payload?.classes?.map { it.groupId }?.distinct()?.sortedBy { it }
        return Resource(groups, resource.status)
    }

    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
        groupId: String,
    ): Resource<Timetable<TimetableOwner.StudyLine>> {
        val resource = super.getTimetable(year, semesterId, ownerId)
        val filteredClasses = resource.payload?.classes?.filter { it.groupId == groupId }
        val timetable = filteredClasses?.let { resource.payload.copy(classes = filteredClasses) }
        return Resource(timetable, resource.status)
    }

    override suspend fun changeTimetableClassVisibility(
        timetableClassId: String,
    ) {
        super.changeTimetableClassVisibility(timetableClassId)
    }

    override suspend fun invalidate(year: Int, semesterId: String) {
        super.invalidate(year, semesterId)
    }

    override suspend fun getOwnersFromCache(
        configurationId: String,
    ): List<TimetableOwner.StudyLine> {
        val entities = studyLineDao.getAll(configurationId)
        return entities.map(::mapEntityToOwner)
    }

    override suspend fun saveOwnerInCache(owner: TimetableOwner.StudyLine) {
        val entity = mapOwnerToEntity(owner)
        studyLineDao.insert(entity)
    }

    override suspend fun getOwnersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.StudyLine>> {
        val configurationId = year.toString() + semesterId
        val resource = studyLinesApi.getOwnersHtml(year, semesterId)
        val ownersHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val tables = HtmlParser.extractTables(ownersHtml)
        val owners = tables.mapIndexed { tableIndex, table ->
            table.rows.mapNotNull { row ->
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
        }.flatten()

        return when {
            owners.isEmpty() -> Resource(null, Status.Error)
            else -> Resource(owners, Status.Success)
        }
    }

    override suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        owner: TimetableOwner.StudyLine,
    ): Resource<Timetable<TimetableOwner.StudyLine>> {
        val configurationId = year.toString() + semesterId
        val resource = studyLinesApi.getTimetableHtml(year, semesterId, owner.id)
        val timetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val tables = HtmlParser.extractTables(timetableHtml)
        val joinedTables = tables.map { table ->
            val groupTitle = table.title.substringBefore(String.SLASH)
            val groupId = groupTitle.substringAfter(String.SPACE)
            table.copy(title = groupId)
        }.groupBy { it.title }.map { (groupId, tables) ->
            Table(
                title = groupId,
                rows = tables.flatMap { it.rows }.distinct()
            )
        }

        val rowsCount = joinedTables.sumOf { table -> table.rows.size }
        val classes = joinedTables.map { table ->
            val groupId = table.title
            table.rows.mapNotNull { row ->
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
                    startHour = "$startHour:00",
                    endHour = "$endHour:00",
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
        }.flatten()

        return when {
            classes.size != rowsCount -> Resource(null, Status.Error)
            else -> Resource(Timetable(owner, classes), Status.Success)
        }
    }

    override fun sortOwners(
        owners: List<TimetableOwner.StudyLine>,
    ): List<TimetableOwner.StudyLine> {
        return owners.sortedBy { it.name }
    }

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
