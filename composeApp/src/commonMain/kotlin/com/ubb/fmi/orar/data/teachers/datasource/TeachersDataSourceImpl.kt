package com.ubb.fmi.orar.data.teachers.datasource

import com.ubb.fmi.orar.data.database.dao.TeacherDao
import com.ubb.fmi.orar.data.database.model.TeacherClassEntity
import com.ubb.fmi.orar.data.database.model.TeacherEntity
import com.ubb.fmi.orar.data.teachers.api.TeachersApi
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherTimetable
import com.ubb.fmi.orar.data.teachers.model.TeacherClass
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.domain.extensions.BLANK
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

class TeachersDataSourceImpl(
    private val teachersApi: TeachersApi,
    private val teacherDao: TeacherDao
) : TeachersDataSource {

    override suspend fun getTeachers(
        year: Int,
        semesterId: String
    ): Resource<List<Teacher>> {
        val cachedTeachers = getTeachersFromCache()

        return when {
            cachedTeachers.isNotEmpty() -> {
                println("TESTMESSAGE Teacher: from cache")
                Resource(cachedTeachers, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Teacher: from API")
                val apiTeachersResource = getTeachersFromApi(year, semesterId)
                apiTeachersResource.payload?.forEach { teacher ->
                    val teacherEntity = mapTeacherToEntity(teacher)
                    teacherDao.insertTeacher(teacherEntity)
                }

                apiTeachersResource
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
                println("TESTMESSAGE Teacher: from cache")
                Resource(cachedTimetables, Status.Success)
            }

            else -> {
                println("TESTMESSAGE Teacher: from API")
                val apiTimetablesResource = getTimetablesFromApi(year, semesterId)
                apiTimetablesResource.payload?.forEach { timetable ->
                    val teacherEntity = mapTeacherToEntity(timetable.teacher)
                    val classesEntities = mapClassesToEntities(
                        teacherId = timetable.teacher.id,
                        classes = timetable.classes
                    )

                    teacherDao.insertTeacher(teacherEntity)
                    teacherDao.insertTeacherClasses(classesEntities)
                }

                apiTimetablesResource
            }
        }
    }

    private suspend fun getTeachersFromCache(): List<Teacher> {
        val entities = teacherDao.getAllTeachers()
        return entities.map(::mapEntityToTeacher)
    }

    private suspend fun getTimetablesFromCache(): List<TeacherTimetable> {
        val entities = teacherDao.getAllTeachersWithClasses()
        return entities.map { (teacherEntity, classesEntities) ->
            TeacherTimetable(
                teacher = mapEntityToTeacher(teacherEntity),
                classes = mapEntitiesToClasses(classesEntities)
            )
        }
    }

    private suspend fun getTimetablesFromApi(
        year: Int,
        semesterId: String
    ): Resource<List<TeacherTimetable>> {
        return withContext(Dispatchers.Default) {
            val teachersResource = getTeachersFromApi(year, semesterId)
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

    suspend fun getTeacherTimetableFromApi(
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
            val hoursCell = row.cells.getOrNull(HOURS_INDEX) ?: return@mapNotNull null
            val frequencyCell = row.cells.getOrNull(FREQUENCY_INDEX) ?: return@mapNotNull null
            val roomCell = row.cells.getOrNull(ROOM_INDEX) ?: return@mapNotNull null
            val studyLineCell = row.cells.getOrNull(STUDY_LINE_INDEX) ?: return@mapNotNull null
            val classTypeCell = row.cells.getOrNull(CLASS_TYPE_INDEX) ?: return@mapNotNull null
            val subjectCell = row.cells.getOrNull(SUBJECT_INDEX) ?: return@mapNotNull null

            val id = listOf(
                dayCell.value,
                hoursCell.value,
                frequencyCell.value,
                studyLineCell.value,
                classTypeCell.value,
                subjectCell.id,
                subjectCell.value,
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            TeacherClass(
                id = id,
                day = dayCell.value,
                hours = hoursCell.value,
                frequencyId = frequencyCell.value,
                roomId = roomCell.id,
                studyLineId = studyLineCell.value,
                classTypeId = classTypeCell.value,
                subjectId = subjectCell.id,
                subjectName = subjectCell.value,
            )
        }

        return when {
            classes.isNullOrEmpty() -> Resource(null, Status.Error)
            else -> Resource(TeacherTimetable(teacher, classes), Status.Success)
        }
    }

    suspend fun getTeachersFromApi(year: Int, semesterId: String): Resource<List<Teacher>> {
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
                hours = teacherClassEntity.hours,
                frequencyId = teacherClassEntity.frequencyId,
                roomId = teacherClassEntity.roomId,
                studyLineId = teacherClassEntity.studyLineId,
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
                hours = teacherClass.hours,
                frequencyId = teacherClass.frequencyId,
                studyLineId = teacherClass.studyLineId,
                classTypeId = teacherClass.classTypeId,
                subjectId = teacherClass.subjectId,
                subjectName = teacherClass.subjectName
            )
        }
    }

    companion object {
        // Teacher timetable column indexes
        private const val DAY_INDEX = 0
        private const val HOURS_INDEX = 1
        private const val FREQUENCY_INDEX = 2
        private const val ROOM_INDEX = 3
        private const val STUDY_LINE_INDEX = 5
        private const val CLASS_TYPE_INDEX = 6
        private const val SUBJECT_INDEX = 7

        private const val NULL = "null"
    }
}
