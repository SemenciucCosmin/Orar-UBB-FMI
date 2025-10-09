package com.ubb.fmi.orar.data.timetable.datasource

import Logger
import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.TimetableClassEntity
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.TimetableClass

/**
 * Data source for managing all timetable classes
 */
class TimetableClassDataSourceImpl(
    private val timetableClassDao: TimetableClassDao,
    private val logger: Logger,
) : TimetableClassDataSource {

    /**
     * Retrieve list of [TimetableClass] for specific [ownerId] from cache
     * by [configurationId]
     */
    override suspend fun getTimetableClassesFromCache(
        configurationId: String,
        ownerId: String,
    ): List<TimetableClass> {
        val classEntities = timetableClassDao.getAllByConfigurationAndOwner(
            configurationId = configurationId,
            ownerId = ownerId
        )

        return classEntities.map(::mapEntityToClass)
    }

    /**
     * Saves new list of [TimetableClass] to cache
     */
    override suspend fun saveTimetableClassesInCache(classes: List<TimetableClass>) {
        val classEntities = classes.map(::mapClassToEntity)
        classEntities.forEach { timetableClassDao.insert(it) }
    }

    /**
     * Sorts [TimetableClass] objects by day order index, start hour and end hour
     */
    override fun sortTimetableClasses(classes: List<TimetableClass>): List<TimetableClass> {
        return classes.sortedWith(
            compareBy<TimetableClass> { Day.Companion.getById(it.day).orderIndex }
                .thenBy { it.startHour.toIntOrNull() }
                .thenBy { it.endHour.toIntOrNull() }
        )
    }

    /**
     * Change visibility of specific timetable class by [timetableClassId]
     */
    override suspend fun changeTimetableClassVisibility(timetableClassId: String) {
        logger.d(TAG, "changeTimetableClassVisibility for timetableClassId: $timetableClassId")
        val timetableClassEntity = timetableClassDao.getById(timetableClassId)
        val newTimetableClassEntity = timetableClassEntity.copy(
            isVisible = !timetableClassEntity.isVisible
        )

        timetableClassDao.insert(newTimetableClassEntity)
    }

    /**
     * Invalidates all cached data for by [year] and [semesterId]
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        logger.d(TAG, "invalidate classes for year: $year, semester: $semesterId")
        val configurationId = year.toString() + semesterId
        timetableClassDao.deleteAll(configurationId)
    }

    /**
     * Maps a [TimetableClass] to a [TimetableClassEntity]
     */
    private fun mapClassToEntity(timetableClass: TimetableClass): TimetableClassEntity {
        return TimetableClassEntity(
            id = timetableClass.id,
            day = timetableClass.day,
            startHour = timetableClass.startHour,
            endHour = timetableClass.endHour,
            frequencyId = timetableClass.frequencyId,
            room = timetableClass.room,
            studyLine = timetableClass.field,
            participant = timetableClass.participant,
            classType = timetableClass.classType,
            ownerId = timetableClass.ownerId,
            subject = timetableClass.subject,
            teacher = timetableClass.teacher,
            isVisible = timetableClass.isVisible,
            configurationId = timetableClass.configurationId
        )
    }

    /**
     * Maps a [TimetableClassEntity] to a [TimetableClass]
     */
    private fun mapEntityToClass(entity: TimetableClassEntity): TimetableClass {
        return TimetableClass(
            id = entity.id,
            day = entity.day,
            startHour = entity.startHour,
            endHour = entity.endHour,
            frequencyId = entity.frequencyId,
            room = entity.room,
            field = entity.studyLine,
            participant = entity.participant,
            classType = entity.classType,
            ownerId = entity.ownerId,
            subject = entity.subject,
            teacher = entity.teacher,
            isVisible = entity.isVisible,
            configurationId = entity.configurationId,
        )
    }

    companion object {
        private const val TAG = "TimetableEventDataSource"
    }
}
