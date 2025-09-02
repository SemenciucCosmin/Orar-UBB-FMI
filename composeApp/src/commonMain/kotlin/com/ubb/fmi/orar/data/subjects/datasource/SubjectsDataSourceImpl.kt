package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.database.dao.SubjectDao
import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.subjects.api.SubjectsApi
import com.ubb.fmi.orar.data.timetable.datasource.TimetableDataSource
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import okio.ByteString.Companion.encodeUtf8

class SubjectsDataSourceImpl(
    private val subjectsApi: SubjectsApi,
    private val subjectDao: SubjectDao,
    timetableClassDao: TimetableClassDao,
) : SubjectsDataSource, TimetableDataSource<TimetableOwner.Subject>(timetableClassDao) {

    override suspend fun getOwners(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.Subject>> {
        return super.getOwners(year, semesterId)
    }

    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<TimetableOwner.Subject>> {
        return super.getTimetable(year, semesterId, ownerId)
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
    ): List<TimetableOwner.Subject> {
        val entities = subjectDao.getAll(configurationId)
        return entities.map(::mapEntityToOwner)
    }

    override suspend fun saveOwnerInCache(owner: TimetableOwner.Subject) {
        val entity = mapOwnerToEntity(owner)
        subjectDao.insert(entity)
    }

    override suspend fun getOwnersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<TimetableOwner.Subject>> {
        val configurationId = year.toString() + semesterId
        val resource = subjectsApi.getOwnersHtml(year, semesterId)
        val ownersHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val table = HtmlParser.extractTables(ownersHtml).firstOrNull()
        val owners = table?.rows?.mapNotNull { row ->
            val idCell = row.cells.getOrNull(ID_INDEX) ?: return@mapNotNull null
            val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null

            TimetableOwner.Subject(
                id = idCell.value,
                name = nameCell.value,
                configurationId = configurationId,
            )
        }

        return when {
            owners.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(owners, Status.Success)
        }
    }

    override suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        owner: TimetableOwner.Subject,
    ): Resource<Timetable<TimetableOwner.Subject>> {
        val configurationId = year.toString() + semesterId
        val resource = subjectsApi.getTimetableHtml(year, semesterId, owner.id)
        val timetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val table = HtmlParser.extractTables(timetableHtml).firstOrNull()
        val classes = table?.rows?.mapNotNull { row ->
            val dayCell = row.cells.getOrNull(DAY_INDEX) ?: return@mapNotNull null
            val intervalCell = row.cells.getOrNull(INTERVAL_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val participantCell = row.cells.getOrNull(PARTICIPANT_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val teacherCell = row.cells.getOrNull(TEACHER_INDEX) ?: return@mapNotNull null
            val intervals = intervalCell.value.split(String.DASH)
            val startHour = intervals.getOrNull(START_HOUR_INDEX) ?: return@mapNotNull null
            val endHour = intervals.getOrNull(END_HOUR_INDEX) ?: return@mapNotNull null

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                roomCell.value,
                studyLineCell.value,
                participantCell.value,
                classTypeCell.value,
                teacherCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            TimetableClass(
                id = id,
                day = dayCell.value,
                startHour = startHour,
                endHour = endHour,
                frequencyId = frequencyCell.value,
                room = roomCell.value,
                subject = owner.name,
                field = studyLineCell.value,
                participant = participantCell.value,
                classType = classTypeCell.value,
                ownerId = owner.id,
                groupId = String.BLANK,
                ownerTypeId = owner.type.id,
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

    override fun sortOwners(
        owners: List<TimetableOwner.Subject>,
    ): List<TimetableOwner.Subject> {
        return owners.sortedBy { it.name }
    }

    private fun mapOwnerToEntity(owner: TimetableOwner.Subject): SubjectEntity {
        return SubjectEntity(
            id = owner.id,
            name = owner.name,
            configurationId = owner.configurationId
        )
    }

    private fun mapEntityToOwner(entity: SubjectEntity): TimetableOwner.Subject {
        return TimetableOwner.Subject(
            id = entity.id,
            name = entity.name,
            configurationId = entity.configurationId
        )
    }

    companion object {
        // Subjects map column indexes
        private const val ID_INDEX = 0
        private const val NAME_INDEX = 1

        // Subject timetable column indexes
        private const val DAY_INDEX = 0
        private const val INTERVAL_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val STUDY_LINE_INDEX = 4
        private const val PARTICIPANT_INDEX = 5
        private const val CLASS_TYPE_INDEX = 6
        private const val TEACHER_INDEX = 7

        // Interval indexes
        private const val START_HOUR_INDEX = 0
        private const val END_HOUR_INDEX = 1
    }
}
