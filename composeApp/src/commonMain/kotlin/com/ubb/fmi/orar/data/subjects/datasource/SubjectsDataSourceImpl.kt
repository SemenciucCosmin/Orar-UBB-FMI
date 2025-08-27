package com.ubb.fmi.orar.data.subjects.datasource

import com.ubb.fmi.orar.data.database.dao.SubjectClassDao
import com.ubb.fmi.orar.data.database.dao.SubjectDao
import com.ubb.fmi.orar.data.database.model.SubjectClassEntity
import com.ubb.fmi.orar.data.database.model.SubjectEntity
import com.ubb.fmi.orar.data.subjects.api.SubjectsApi
import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.data.subjects.model.SubjectTimetable
import com.ubb.fmi.orar.data.subjects.model.SubjectClass
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.encodeUtf8
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.orEmpty

class SubjectsDataSourceImpl(
    private val subjectsApi: SubjectsApi,
    private val subjectDao: SubjectDao,
    private val subjectClassDao: SubjectClassDao,
) : SubjectsDataSource {
    
    override suspend fun getSubjects(
        year: Int,
        semesterId: String
    ): Resource<List<Subject>> {
        val cachedSubjects = getSubjectsFromCache()

        return when {
            cachedSubjects.isNotEmpty() -> {
                println("TESTMESSAGE Subject: from cache")
                val orderedSubjects = cachedSubjects.sortedBy { it.name }
                Resource(orderedSubjects, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Subject: from API")
                val apiSubjectsResource = getSubjectsFromApi(year, semesterId)
                apiSubjectsResource.payload?.forEach { subject ->
                    val subjectEntity = mapSubjectToEntity(subject)
                    subjectDao.insertSubject(subjectEntity)
                }

                val orderedSubjects = apiSubjectsResource.payload?.sortedBy { it.name }
                Resource(orderedSubjects, apiSubjectsResource.status)
            }
        }
    }

    override suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<SubjectTimetable>> {
        val cachedTimetables = getTimetablesFromCache()

        return when {
            cachedTimetables.isNotEmpty() -> {
                println("TESTMESSAGE Subject: from cache")
                Resource(cachedTimetables, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Subject: from API")
                val apiTimetablesResource = getTimetablesFromApi(year, semesterId)
                apiTimetablesResource.payload?.forEach { timetable ->
                    val subjectEntity = mapSubjectToEntity(timetable.subject)
                    val classesEntities = mapClassesToEntities(
                        subjectId = timetable.subject.id,
                        classes = timetable.classes
                    )

                    subjectDao.insertSubject(subjectEntity)
                    classesEntities.forEach { subjectClassDao.insertSubjectClass(it) }
                }

                apiTimetablesResource
            }
        }
    }

    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        subjectId: String
    ): Resource<SubjectTimetable> {
        val subject = getSubjects(year, semesterId).payload?.firstOrNull { it.id == subjectId }
        val cachedTimetable = subject?.let { getTimetableFromCache(subject) }

        return when {
            cachedTimetable?.classes?.isNotEmpty() == true -> {
                println("TESTMESSAGE Subject Timetable: from cache")
                Resource(cachedTimetable, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Subject Timetable: from API")
                if (subject == null) return Resource(null, Status.Error)
                val apiTimetableResource = getSubjectTimetableFromApi(year, semesterId, subject)
                apiTimetableResource.payload?.let { timetable ->
                    val subjectEntity = mapSubjectToEntity(timetable.subject)
                    val classesEntities = mapClassesToEntities(
                        subjectId = timetable.subject.id,
                        classes = timetable.classes
                    )

                    subjectDao.insertSubject(subjectEntity)
                    classesEntities.forEach { subjectClassDao.insertSubjectClass(it) }
                }

                apiTimetableResource
            }
        }
    }

    override suspend fun changeTimetableClassVisibility(timetableClassId: String) {
        val subjectClassEntity = subjectClassDao.getSubjectClass(timetableClassId)
        val newSubjectClassEntity = subjectClassEntity.copy(
            isVisible = !subjectClassEntity.isVisible
        )

        subjectClassDao.insertSubjectClass(newSubjectClassEntity)
    }

    private suspend fun getSubjectsFromCache(): List<Subject> {
        val entities = subjectDao.getAllSubjects()
        return entities.map(::mapEntityToSubject)
    }

    private suspend fun getTimetableFromCache(subject: Subject): SubjectTimetable {
        val subjectClassEntities = subjectClassDao.getSubjectClasses(subject.id)
        return SubjectTimetable(
            subject = subject,
            classes = mapEntitiesToClasses(subjectClassEntities),
        )
    }

    private suspend fun getTimetablesFromCache(): List<SubjectTimetable> {
        val subjectEntities = subjectDao.getAllSubjects()
        val subjectClassEntities = subjectClassDao.getAllSubjectClasses()
        val groupedSubjectClassEntities = subjectClassEntities.groupBy { it.subjectId }
        val subjectWithClassesEntities = subjectEntities.associateWith { subjectEntity ->
            groupedSubjectClassEntities[subjectEntity.id].orEmpty()
        }.filter { it.value.isNotEmpty() }

        return subjectWithClassesEntities.map { (subjectEntity, classesEntities) ->
            SubjectTimetable(
                subject = mapEntityToSubject(subjectEntity),
                classes = mapEntitiesToClasses(classesEntities),
            )
        }
    }

    private suspend fun getSubjectsFromApi(year: Int, semesterId: String): Resource<List<Subject>> {
        val resource = subjectsApi.getSubjectsMapHtml(
            year = year,
            semesterId = semesterId
        )

        val subjectsMapHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val subjectsTable = HtmlParser.extractTables(
            html = subjectsMapHtml
        ).firstOrNull()

        val subjects = subjectsTable?.rows?.mapNotNull { row ->
            val idCell = row.cells.getOrNull(ID_INDEX) ?: return@mapNotNull null
            val nameCell = row.cells.getOrNull(NAME_INDEX) ?: return@mapNotNull null

            Subject(
                id = idCell.value,
                name = nameCell.value,
            )
        }

        return when {
            subjects.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(subjects, Status.Success)
        }
    }
    
    private suspend fun getTimetablesFromApi(
        year: Int,
        semesterId: String
    ): Resource<List<SubjectTimetable>> {
        return withContext(Dispatchers.Default) {
            val subjectsResource = getSubjectsFromApi(year, semesterId)
            val subjects = subjectsResource.payload ?: emptyList()
            val subjectTimetablesResources = subjects.map { subject ->
                async { getSubjectTimetableFromApi(year, semesterId, subject) }
            }.awaitAll()

            val subjectTimetables = subjectTimetablesResources.mapNotNull { it.payload }
            val errorStatus = subjectTimetablesResources.map {
                it.status
            }.firstOrNull { it.isError() }

            return@withContext when {
                subjectsResource.status.isError() -> Resource(null, subjectsResource.status)
                errorStatus != null -> Resource(null, errorStatus)
                else -> Resource(subjectTimetables, Status.Success)
            }
        }
    }

    private suspend fun getSubjectTimetableFromApi(
        year: Int,
        semesterId: String,
        subject: Subject
    ): Resource<SubjectTimetable> {
        val resource = subjectsApi.getSubjectTimetableHtml(
            year = year,
            semesterId = semesterId,
            subjectId = subject.id
        )

        val subjectTimetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val subjectTable = HtmlParser.extractTables(
            html = subjectTimetableHtml
        ).firstOrNull()

        val classes = subjectTable?.rows?.mapNotNull { row ->
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

            val participantId = when {
                participantCell.value.contains(SEMIGROUP_1_ID) -> SEMIGROUP_1_ID
                participantCell.value.contains(SEMIGROUP_2_ID) -> SEMIGROUP_2_ID
                participantCell.value.all { it.isDigit() } -> WHOLE_GROUP_ID
                else -> WHOLE_YEAR_ID
            }
            
            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                studyLineCell.value,
                participantCell.value,
                classTypeCell.value,
                teacherCell.id,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            SubjectClass(
                id = id,
                day = dayCell.value,
                startHour = "$startHour:00",
                endHour = "$endHour:00",
                frequencyId = frequencyCell.value,
                roomId = roomCell.id,
                studyLineId = studyLineCell.value,
                participantId = participantId,
                participantName = participantCell.value,
                classTypeId = classTypeCell.value,
                teacherId = teacherCell.id,
                isVisible = true
            )
        }

        return when {
            classes.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(SubjectTimetable(subject, classes), Status.Success)
        }
    }

    private fun mapEntityToSubject(subjectEntity: SubjectEntity): Subject {
        return Subject(
            id = subjectEntity.id,
            name = subjectEntity.name,
        )
    }

    private fun mapEntitiesToClasses(entities: List<SubjectClassEntity>): List<SubjectClass> {
        return entities.map { subjectClassEntity ->
            SubjectClass(
                id = subjectClassEntity.id,
                day = subjectClassEntity.day,
                startHour = subjectClassEntity.startHour,
                endHour = subjectClassEntity.endHour,
                frequencyId = subjectClassEntity.frequencyId,
                roomId = subjectClassEntity.roomId,
                studyLineId = subjectClassEntity.studyLineId,
                participantId = subjectClassEntity.participantId,
                participantName = subjectClassEntity.participantName,
                classTypeId = subjectClassEntity.classTypeId,
                teacherId = subjectClassEntity.teacherId,
                isVisible = subjectClassEntity.isVisible,
            )
        }
    }

    private fun mapSubjectToEntity(subject: Subject): SubjectEntity {
        return SubjectEntity(
            id = subject.id,
            name = subject.name,
        )
    }

    private fun mapClassesToEntities(
        subjectId: String,
        classes: List<SubjectClass>
    ): List<SubjectClassEntity> {
        return classes.map { subjectClass ->
            SubjectClassEntity(
                id = subjectClass.id,
                subjectId = subjectId,
                roomId = subjectClass.roomId,
                day = subjectClass.day,
                startHour = subjectClass.startHour,
                endHour = subjectClass.endHour,
                frequencyId = subjectClass.frequencyId,
                studyLineId = subjectClass.studyLineId,
                participantId = subjectClass.participantId,
                participantName = subjectClass.participantName,
                classTypeId = subjectClass.classTypeId,
                teacherId = subjectClass.teacherId,
                isVisible = subjectClass.isVisible,
            )
        }
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

        // ParticipantIds
        private const val SEMIGROUP_1_ID = "/1"
        private const val SEMIGROUP_2_ID = "/2"
        private const val WHOLE_GROUP_ID = "whole_group"
        private const val WHOLE_YEAR_ID = "whole_year"
    }
}
