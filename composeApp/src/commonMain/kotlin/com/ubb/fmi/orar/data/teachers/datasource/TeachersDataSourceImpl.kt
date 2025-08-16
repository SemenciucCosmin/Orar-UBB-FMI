package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.database.dao.TeacherClassDao
import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.model.TeacherClassEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.teachers.api.TeachersApi
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherTimetable
import com.ubb.fmi.orar.data.teachers.model.TeacherClass
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.COLON
import com.ubb.fmi.orar.domain.extensions.DASH
import com.ubb.fmi.orar.domain.extensions.PIPE
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.htmlparser.HtmlParser
import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.Status
import com.ubb.fmi.orar.network.model.isError
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import okio.ByteString.Companion.encodeUtf8
import kotlin.collections.component1
import kotlin.collections.component2
import kotlin.collections.orEmpty

class TeachersDataSourceImpl(
    private val teachersApi: TeachersApi,
    private val teacherDao: TeacherDao,
    private val teacherClassDao: TeacherClassDao,
) : TeachersDataSource {

    override suspend fun getTeachers(
        year: Int,
        semesterId: String
    ): Resource<List<Teacher>> {
        val cachedTeachers = getTeachersFromCache()

        return when {
            cachedTeachers.isNotEmpty() -> {
                println("TESTMESSAGE Teacher: from cache")
                val orderedTeachers = cachedTeachers.sortedWith(
                    compareBy<Teacher> {
                        TeacherTitle.getById(it.titleId).ordinal
                    }.thenBy { it.name }
                )

                Resource(orderedTeachers, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Teacher: from API")
                val apiTeachersResource = getTeachersFromApi(year, semesterId)
                apiTeachersResource.payload?.forEach { teacher ->
                    val teacherEntity = mapTeacherToEntity(teacher)
                    teacherDao.insertTeacher(teacherEntity)
                }

                val orderedTeachers = apiTeachersResource.payload?.sortedWith(
                    compareBy<Teacher> {
                        TeacherTitle.getById(it.titleId).ordinal
                    }.thenBy { it.name }
                )

                Resource(orderedTeachers, apiTeachersResource.status)
            }
        }
    }

    override suspend fun getTimetables(
        year: Int,
        semesterId: String
    ): Resource<List<TeacherTimetable>> {
        val cachedTimetables = getTimetablesFromCache()

        return when {
            cachedTimetables.isNotEmpty() -> {
                println("TESTMESSAGE Teacher Timetable: from cache")
                Resource(cachedTimetables, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Teacher Timetable: from API")
                val apiTimetablesResource = getTimetablesFromApi(year, semesterId)
                apiTimetablesResource.payload?.forEach { timetable ->
                    val teacherEntity = mapTeacherToEntity(timetable.teacher)
                    val classesEntities = mapClassesToEntities(
                        teacherId = timetable.teacher.id,
                        classes = timetable.classes
                    )

                    teacherDao.insertTeacher(teacherEntity)
                    classesEntities.forEach { teacherClassDao.insertTeacherClass(it) }
                }

                apiTimetablesResource
            }
        }
    }

    override suspend fun getTimetable(
        year: Int,
        semesterId: String,
        teacherId: String
    ): Resource<TeacherTimetable> {
        val teacher = getTeachers(year, semesterId).payload?.firstOrNull { it.id == teacherId }
        val cachedTimetable = teacher?.let { getTimetableFromCache(teacher) }

        return when {
            cachedTimetable?.classes?.isNotEmpty() == true -> {
                println("TESTMESSAGE Teacher Timetable: from cache")
                Resource(cachedTimetable, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Teacher Timetable: from API")
                if (teacher == null) return Resource(null, Status.Error)
                val apiTimetableResource = getTeacherTimetableFromApi(year, semesterId, teacher)
                apiTimetableResource.payload?.let { timetable ->
                    val teacherEntity = mapTeacherToEntity(timetable.teacher)
                    val classesEntities = mapClassesToEntities(
                        teacherId = timetable.teacher.id,
                        classes = timetable.classes
                    )

                    teacherDao.insertTeacher(teacherEntity)
                    classesEntities.forEach { teacherClassDao.insertTeacherClass(it) }
                }

                apiTimetableResource
            }
        }
    }

    private suspend fun getTeachersFromCache(): List<Teacher> {
        val entities = teacherDao.getAllTeachers()
        return entities.map(::mapEntityToTeacher)
    }

    private suspend fun getTimetableFromCache(teacher: Teacher): TeacherTimetable {
        val teacherClassEntities = teacherClassDao.getTeacherClasses(teacher.id)
        return TeacherTimetable(
            teacher = teacher,
            classes = mapEntitiesToClasses(teacherClassEntities),
        )
    }

    private suspend fun getTimetablesFromCache(): List<TeacherTimetable> {
        val teacherEntities = teacherDao.getAllTeachers()
        val teacherClassEntities = teacherClassDao.getAllTeacherClasses()
        val groupedTeacherClassEntities = teacherClassEntities.groupBy { it.teacherId }
        val teacherWithClassesEntities = teacherEntities.associateWith { teacherEntity ->
            groupedTeacherClassEntities[teacherEntity.id].orEmpty()
        }.filter { it.value.isNotEmpty() }

        return teacherWithClassesEntities.map { (teacherEntity, classesEntities) ->
            TeacherTimetable(
                teacher = mapEntityToTeacher(teacherEntity),
                classes = mapEntitiesToClasses(classesEntities),
            )
        }
    }

    private suspend fun getTeachersFromApi(
        year: Int,
        semesterId: String
    ): Resource<List<Teacher>> {
        val resource = teachersApi.getTeachersHtml(
            year = year,
            semesterId = semesterId
        )

        val teachersHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val teachersTable = HtmlParser.extractTables(
            html = teachersHtml
        ).firstOrNull()

        val teachersCells = teachersTable?.rows?.map { it.cells }?.flatten()
        val teachers = teachersCells?.mapNotNull { cell ->
            val title = TeacherTitle.entries.firstOrNull {
                cell.value.contains(it.id)
            } ?: return@mapNotNull null

            val name = cell.value
                .replace(title.id, String.BLANK)
                .replaceFirst(String.SPACE, String.BLANK)

            Teacher(
                id = cell.id,
                name = name,
                titleId = title.id
            )
        }?.filter { it.name != NULL }

        return when {
            teachers.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(teachers, Status.Success)
        }
    }

    private suspend fun getTimetablesFromApi(
        year: Int,
        semesterId: String
    ): Resource<List<TeacherTimetable>> {
        return withContext(Dispatchers.Default) {
            val teachersResource = getTeachers(year, semesterId)
            val teachers = teachersResource.payload ?: emptyList()
            val teacherTimetablesResources = teachers.map { room ->
                async { getTeacherTimetableFromApi(year, semesterId, room) }
            }.awaitAll()

            val teachersTimetables = teacherTimetablesResources.mapNotNull { it.payload }
            val errorStatus = teacherTimetablesResources.map {
                it.status
            }.firstOrNull { it.isError() }

            return@withContext when {
                teachersResource.status.isError() -> Resource(null, teachersResource.status)
                errorStatus != null -> Resource(null, errorStatus)
                else -> Resource(teachersTimetables, Status.Success)
            }
        }
    }

    private suspend fun getTeacherTimetableFromApi(
        year: Int,
        semesterId: String,
        teacher: Teacher
    ): Resource<TeacherTimetable> {
        val resource = teachersApi.getTeacherTimetableHtml(
            year = year,
            semesterId = semesterId,
            teacherId = teacher.id
        )

        val teacherTimetableHtml = resource.payload ?: return Resource(null, Status.NotFoundError)
        val teacherTable = HtmlParser.extractTables(
            html = teacherTimetableHtml
        ).firstOrNull()

        val classes = teacherTable?.rows?.mapNotNull { row ->
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

            val roomId = when {
                roomCell.value == NULL -> String.BLANK
                else -> roomCell.value
            }

            val studyLineId = when {
                studyLineCell.value == NULL -> String.BLANK
                else -> studyLineCell.value
            }

            val participantName = when {
                participantCell.value == NULL -> String.BLANK
                else -> participantCell.value
            }

            val participantId = when {
                participantCell.value.contains(SEMIGROUP_1_ID) -> SEMIGROUP_1_ID
                participantCell.value.contains(SEMIGROUP_2_ID) -> SEMIGROUP_2_ID
                participantCell.value.all { it.isDigit() } -> WHOLE_GROUP_ID
                else -> WHOLE_YEAR_ID
            }

            val (classTypeId, subjectName) = when {
                subjectCell.value.contains(CLASS_TYPE_STAFF_ID) -> {
                    val subjectName = subjectCell.value.split(
                        String.COLON
                    ).lastOrNull()?.replace(
                        String.SPACE, String.BLANK
                    ) ?: subjectCell.value

                    CLASS_TYPE_STAFF_ID to subjectName
                }

                else -> classTypeCell.value to subjectCell.value
            }

            val id = listOf(
                dayCell.value,
                intervalCell.value,
                frequencyCell.value,
                studyLineCell.value,
                participantCell.value,
                classTypeCell.value,
                subjectCell.id,
                subjectCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()



            TeacherClass(
                id = id,
                day = dayCell.value,
                startHour = "$startHour:00",
                endHour = "$endHour:00",
                frequencyId = frequencyCell.value,
                roomId = roomId,
                studyLineId = studyLineId,
                participantId = participantId,
                participantName = participantName,
                classTypeId = classTypeId,
                subjectId = subjectCell.id,
                subjectName = subjectName,
            )
        }

        return when {
            classes.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(TeacherTimetable(teacher, classes), Status.Success)
        }
    }

    private fun mapEntityToTeacher(teacherEntity: TeacherEntity): Teacher {
        return Teacher(
            id = teacherEntity.id,
            name = teacherEntity.name,
            titleId = teacherEntity.titleId
        )
    }

    private fun mapEntitiesToClasses(entities: List<TeacherClassEntity>): List<TeacherClass> {
        return entities.map { teacherClassEntity ->
            TeacherClass(
                id = teacherClassEntity.id,
                day = teacherClassEntity.day,
                startHour = teacherClassEntity.startHour,
                endHour = teacherClassEntity.endHour,
                frequencyId = teacherClassEntity.frequencyId,
                roomId = teacherClassEntity.roomId,
                studyLineId = teacherClassEntity.studyLineId,
                participantId = teacherClassEntity.participantId,
                participantName = teacherClassEntity.participantName,
                classTypeId = teacherClassEntity.classTypeId,
                subjectId = teacherClassEntity.subjectId,
                subjectName = teacherClassEntity.subjectName,
            )
        }
    }

    private fun mapTeacherToEntity(teacher: Teacher): TeacherEntity {
        return TeacherEntity(
            id = teacher.id,
            name = teacher.name,
            titleId = teacher.titleId
        )
    }

    private fun mapClassesToEntities(
        teacherId: String,
        classes: List<TeacherClass>
    ): List<TeacherClassEntity> {
        return classes.map { teacherClass ->
            TeacherClassEntity(
                id = teacherClass.id,
                teacherId = teacherId,
                roomId = teacherClass.roomId,
                day = teacherClass.day,
                startHour = teacherClass.startHour,
                endHour = teacherClass.endHour,
                frequencyId = teacherClass.frequencyId,
                studyLineId = teacherClass.studyLineId,
                participantId = teacherClass.participantId,
                participantName = teacherClass.participantName,
                classTypeId = teacherClass.classTypeId,
                subjectId = teacherClass.subjectId,
                subjectName = teacherClass.subjectName
            )
        }
    }

    companion object {
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

        // ParticipantIds
        private const val SEMIGROUP_1_ID = "/1"
        private const val SEMIGROUP_2_ID = "/2"
        private const val WHOLE_GROUP_ID = "whole_group"
        private const val WHOLE_YEAR_ID = "whole_year"
        private const val NULL = "null"

        // StaffId
        private const val CLASS_TYPE_STAFF_ID = "Colectiv"
    }
}
