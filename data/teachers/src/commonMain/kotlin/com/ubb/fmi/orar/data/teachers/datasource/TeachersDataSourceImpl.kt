package com.ubb.fmi.orar.data.teachers.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.TeachersApi
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.data.timetable.datasource.TimetableDataSource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COLON
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing teacher related information
 */
@Suppress("CyclomaticComplexMethod")
class TeachersDataSourceImpl(
    private val teachersApi: TeachersApi,
    private val teacherDao: TeacherDao,
    private val logger: Logger,
    timetableClassDao: TimetableClassDao,
) : TeachersDataSource, TimetableDataSource<TimetableOwner.Teacher>(
    timetableClassDao = timetableClassDao,
    logger = logger,
    tag = TAG,
) {

    /**
     * Retrieve list of [TimetableOwner.Teacher] objects from cache or API
     * by [year] and [semesterId]
     */
    override suspend fun getOwners(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.Teacher>> {
        return super.getOwners(year, semesterId)
    }

    /**
     * Retrieve timetable of [TimetableOwner.Teacher] for specific teacher from cache or
     * API by [year], [semesterId] and [ownerId]
     */
    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<TimetableOwner.Teacher>> {
        return super.getTimetable(year, semesterId, ownerId)
    }

    /**
     * Change visibility of specific teacher timetable class by [timetableClassId]
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
     * Retrieve list of [TimetableOwner.Teacher] objects from cache by [configurationId]
     */
    override suspend fun getOwnersFromCache(
        configurationId: String,
    ): List<TimetableOwner.Teacher> {
        val entities = teacherDao.getAll(configurationId)
        return entities.map(::mapEntityToOwner)
    }

    /**
     * Saves new teacher [owner] to cache
     */
    override suspend fun saveOwnerInCache(owner: TimetableOwner.Teacher) {
        val entity = mapOwnerToEntity(owner)
        teacherDao.insert(entity)
    }

    /**
     * Retrieve list of [TimetableOwner.Teacher] objects from API by [year] and [semesterId]
     */
    override suspend fun getOwnersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.Teacher>> {
        logger.d(TAG, "getOwnersFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = teachersApi.getOwnersHtml(year, semesterId)

        logger.d(TAG, "getOwnersFromApi resource: $resource")

        val ownersHtml = resource.payload
        val table = ownersHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getOwnersFromApi table: $table")

        val cells = table?.rows?.map { it.cells }?.flatten()
        val owners = cells?.mapNotNull { cell ->
            logger.d(TAG, "getOwnersFromApi cell: $cell")

            val title = TeacherTitle.entries.firstOrNull {
                cell.value.contains(it.id)
            } ?: return@mapNotNull null

            val name = cell.value
                .replace(title.id, String.BLANK)
                .replaceFirst(String.SPACE, String.BLANK)

            TimetableOwner.Teacher(
                id = cell.id,
                name = name,
                titleId = title.id,
                configurationId = configurationId,
            )
        }?.filter { it.name != NULL }

        logger.d(TAG, "getOwnersFromApi owners: $owners")

        return when {
            owners.isNullOrEmpty() -> Resource(null, resource.status)
            else -> Resource(owners, Status.Success)
        }
    }

    /**
     * Retrieve timetable of [TimetableOwner.Teacher] for specific teacher from API
     * by [year], [semesterId] and [owner]
     */
    override suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        owner: TimetableOwner.Teacher,
    ): Resource<Timetable<TimetableOwner.Teacher>> {
        logger.d(TAG, "getTimetableFromApi for year: $year, semester: $semesterId")

        val configurationId = year.toString() + semesterId
        val resource = teachersApi.getTimetableHtml(year, semesterId, owner.id)

        logger.d(TAG, "getTimetableFromApi resource: $resource")

        val timetableHtml = resource.payload
        val table = timetableHtml?.let(HtmlParser::extractTables)?.firstOrNull()

        logger.d(TAG, "getTimetableFromApi table: $table")

        val classes = table?.rows?.mapNotNull { row ->
            logger.d(TAG, "getTimetableFromApi row: $row")
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
            val intervals = intervalCell.value.split(String.DASH)
            val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null

            val room = when {
                roomCell.value == NULL -> String.BLANK
                else -> roomCell.value
            }

            val studyLine = when {
                studyLineCell.value == NULL -> String.BLANK
                else -> studyLineCell.value
            }

            val participant = when {
                participantCell.value == NULL -> String.BLANK
                else -> participantCell.value
            }

            val (classType, subject) = when {
                subjectCell.value.contains(CLASS_TYPE_STAFF_ID) -> {
                    val subject = subjectCell.value.split(
                        String.COLON
                    ).lastOrNull()?.replace(
                        String.SPACE, String.BLANK
                    ) ?: subjectCell.value

                    CLASS_TYPE_STAFF_ID to subject
                }

                else -> classTypeCell.value to subjectCell.value
            }

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                roomCell.value,
                studyLineCell.value,
                participantCell.value,
                classTypeCell.value,
                subjectCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            TimetableClass(
                id = id,
                day = dayCell.value,
                startHour = startHour,
                endHour = endHour,
                frequencyId = frequencyCell.value,
                room = room,
                field = studyLine,
                participant = participant,
                classType = classType,
                ownerId = owner.id,
                groupId = String.BLANK,
                ownerTypeId = owner.type.id,
                subject = subject,
                teacher = owner.name,
                isVisible = true,
                configurationId = configurationId
            )
        }

        logger.d(TAG, "getTimetableFromApi classes: $classes")

        return when {
            classes == null -> Resource(null, resource.status)
            else -> Resource(Timetable(owner, classes), Status.Success)
        }
    }

    /**
     * Sorts rooms by order index and name
     */
    override fun sortOwners(
        owners: List<TimetableOwner.Teacher>,
    ): List<TimetableOwner.Teacher> {
        return owners.sortedWith(
            compareBy<TimetableOwner.Teacher> {
                val title = TeacherTitle.getById(it.titleId)
                title.orderIndex
            }.thenBy { it.name }
        )
    }

    /**
     * Maps a [TimetableOwner.Teacher] to a [TeacherEntity]
     */
    private fun mapOwnerToEntity(owner: TimetableOwner.Teacher): TeacherEntity {
        return TeacherEntity(
            id = owner.id,
            name = owner.name,
            titleId = owner.titleId,
            configurationId = owner.configurationId
        )
    }

    /**
     * Maps a [TeacherEntity] to a [TimetableOwner.Teacher]
     */
    private fun mapEntityToOwner(entity: TeacherEntity): TimetableOwner.Teacher {
        return TimetableOwner.Teacher(
            id = entity.id,
            name = entity.name,
            titleId = entity.titleId,
            configurationId = entity.configurationId
        )
    }

    companion object {
        private const val TAG = "TeachersDataSource"

        // Teacher timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val STUDY_LINE_INDEX = 4
        private const val PARTICIPANT_INDEX = 5
        private const val CLASS_TYPE_INDEX = 6
        private const val SUBJECT_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1

        // StaffId
        private const val CLASS_TYPE_STAFF_ID = "Colectiv"
        private const val NULL = "null"
    }
}
