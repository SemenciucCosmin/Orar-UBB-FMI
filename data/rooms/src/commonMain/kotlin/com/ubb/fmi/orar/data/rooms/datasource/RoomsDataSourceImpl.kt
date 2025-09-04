package com.ubb.fmi.orar.data.rooms.datasource

import com.ubb.fmi.orar.data.database.dao.RoomDao
import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.RoomEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.service.RoomsApi
import com.ubb.fmi.orar.data.timetable.datasource.TimetableDataSource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import okio.ByteString.Companion.encodeUtf8

/**
 * Data source for managing room related information
 */
class RoomsDataSourceImpl(
    private val roomsApi: RoomsApi,
    private val roomDao: RoomDao,
    timetableClassDao: TimetableClassDao,
) : RoomsDataSource, TimetableDataSource<TimetableOwner.Room>(timetableClassDao) {

    /**
     * Retrieve list of [TimetableOwner.Room] objects from cache or API
     * by [year] and [semesterId]
     */
    override suspend fun getOwners(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.Room>> {
        return super.getOwners(year, semesterId)
    }

    /**
     * Retrieve timetable of [TimetableOwner.Room] for specific room from cache or
     * API by [year], [semesterId] and [ownerId]
     */
    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<TimetableOwner.Room>> {
        return super.getTimetable(year, semesterId, ownerId)
    }

    /**
     * Change visibility of specific room timetable class by [timetableClassId]
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
     * Retrieve list of [TimetableOwner.Room] objects from cache by [configurationId]
     */
    override suspend fun getOwnersFromCache(
        configurationId: String,
    ): List<TimetableOwner.Room> {
        val entities = roomDao.getAll(configurationId)
        return entities.map(::mapEntityToOwner)
    }

    /**
     * Saves new room [owner] to cache
     */
    override suspend fun saveOwnerInCache(owner: TimetableOwner.Room) {
        val entity = mapOwnerToEntity(owner)
        roomDao.insert(entity)
    }

    /**
     * Retrieve list of [TimetableOwner.Room] objects from API by [year] and [semesterId]
     */
    override suspend fun getOwnersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.Room>> {
        val configurationId = year.toString() + semesterId
        val resource = roomsApi.getOwnersHtml(year, semesterId)
        val ownersHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val table = HtmlParser.extractTables(ownersHtml).firstOrNull()
        val owners = table?.rows?.mapNotNull { row ->
            val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null
            val locationCell = row.cells.getOrNull(LOCATION_INDEX) ?: return@mapNotNull null

            TimetableOwner.Room(
                id = nameCell.id,
                name = nameCell.value,
                location = locationCell.value,
                configurationId = configurationId,
            )
        }

        return when {
            owners.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(owners, Status.Success)
        }
    }

    /**
     * Retrieve timetable of [TimetableOwner.Room] for specific room from API
     * by [year], [semesterId] and [owner]
     */
    override suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        owner: TimetableOwner.Room,
    ): Resource<Timetable<TimetableOwner.Room>> {
        val configurationId = year.toString() + semesterId
        val resource = roomsApi.getTimetableHtml(year, semesterId, owner.id)
        val timetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val table = HtmlParser.extractTables(timetableHtml).firstOrNull()
        val classes = table?.rows?.mapNotNull { row ->
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null
            val intervals = intervalCell.value.split(String.DASH)
            val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                studyLineCell.value,
                participantCell.value,
                classTypeCell.value,
                subjectCell.value,
                teacherCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            TimetableClass(
                id = id,
                day = dayCell.value,
                startHour = startHour,
                endHour = endHour,
                frequencyId = frequencyCell.value,
                room = owner.name,
                field = studyLineCell.value,
                participant = participantCell.value,
                classType = classTypeCell.value,
                ownerId = owner.id,
                groupId = String.BLANK,
                ownerTypeId = owner.type.id,
                subject = subjectCell.value,
                teacher = teacherCell.value,
                isVisible = true,
                configurationId = configurationId
            )
        }

        return when {
            classes == null -> Resource(null, Status.Error)
            else -> Resource(Timetable(owner, classes), Status.Success)
        }
    }

    /**
     * Sorts rooms by name
     */
    override fun sortOwners(
        owners: List<TimetableOwner.Room>,
    ): List<TimetableOwner.Room> {
        return owners.sortedBy { it.name }
    }

    /**
     * Maps a [TimetableOwner.Room] to a [RoomEntity]
     */
    private fun mapOwnerToEntity(owner: TimetableOwner.Room): RoomEntity {
        return RoomEntity(
            id = owner.id,
            name = owner.name,
            location = owner.location,
            configurationId = owner.configurationId
        )
    }

    /**
     * Maps a [RoomEntity] to a [TimetableOwner.Room]
     */
    private fun mapEntityToOwner(entity: RoomEntity): TimetableOwner.Room {
        return TimetableOwner.Room(
            id = entity.id,
            name = entity.name,
            location = entity.location,
            configurationId = entity.configurationId
        )
    }

    companion object {
        // Rooms map column indexes
        private const val NAME_INDEX = 0
        private const val LOCATION_INDEX = 1

        // Room timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val STUDY_LINE_INDEX = 3
        private const val PARTICIPANT_INDEX = 4
        private const val CLASS_TYPE_INDEX = 5
        private const val SUBJECT_INDEX = 6
        private const val TEACHER_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
