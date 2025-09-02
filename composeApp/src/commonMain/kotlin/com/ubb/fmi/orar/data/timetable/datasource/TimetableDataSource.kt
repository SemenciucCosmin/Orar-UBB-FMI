package com.ubb.fmi.orar.data.timetable.datasource

import com.ubb.fmi.orar.data.database.dao.TimetableClassDao
import com.ubb.fmi.orar.data.database.model.TimetableClassEntity
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.timetable.model.TimetableClass
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner

@Suppress("UNCHECKED_CAST", "TooManyFunctions")
abstract class TimetableDataSource<Owner : TimetableOwner>(
    private val timetableClassDao: TimetableClassDao,
) {
    protected open suspend fun getOwners(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner>> {
        val configurationId = year.toString() + semesterId
        val cachedOwners = getOwnersFromCache(configurationId)

        return when {
            cachedOwners.isNotEmpty() -> {
                val sortedOwners = sortOwners(cachedOwners)
                Resource(sortedOwners, Status.Success)
            }

            else -> {
                val ownersResource = getOwnersFromApi(year, semesterId)
                ownersResource.payload?.forEach { saveOwnerInCache(it) }

                val sortedOwners = ownersResource.payload?.let(::sortOwners)
                Resource(sortedOwners, ownersResource.status)
            }
        }
    }

    protected open suspend fun getTimetable(
        year: Int,
        semesterId: String,
        ownerId: String,
    ): Resource<Timetable<Owner>> {
        val configurationId = year.toString() + semesterId
        val owner = getOwners(year, semesterId).payload?.firstOrNull { it.id == ownerId }
        val cachedTimetable = owner?.let { getTimetableFromCache(configurationId, it) }

        return when {
            cachedTimetable?.classes?.isNotEmpty() == true -> {
                val sortedClasses = sortTimetableClasses(cachedTimetable.classes)
                Resource(cachedTimetable.copy(classes = sortedClasses), Status.Success)
            }

            else -> {
                if (owner == null) return Resource(null, Status.Error)
                val timetableResource = getTimetableFromApi(year, semesterId, owner)
                timetableResource.payload?.let { saveTimetableInCache(it) }

                val timetable = timetableResource.payload?.let { timetable ->
                    val sortedClasses = sortTimetableClasses(timetable.classes)
                    timetable.copy(classes = sortedClasses)
                }

                Resource(timetable, timetableResource.status)
            }
        }
    }

    protected open suspend fun changeTimetableClassVisibility(timetableClassId: String) {
        val timetableClassEntity = timetableClassDao.getById(timetableClassId)
        val newTimetableClassEntity = timetableClassEntity.copy(
            isVisible = !timetableClassEntity.isVisible
        )

        timetableClassDao.insert(newTimetableClassEntity)
    }

    protected open suspend fun invalidate(year: Int, semesterId: String) {
        val configurationId = year.toString() + semesterId
        timetableClassDao.deleteAll(configurationId)
    }

    private suspend fun getTimetableFromCache(
        configurationId: String,
        owner: Owner,
    ): Timetable<Owner> {
        val classEntities = timetableClassDao.getAllByConfigurationAndOwner(
            configurationId = configurationId,
            ownerId = owner.id
        )

        return Timetable(
            owner = owner,
            classes = classEntities.map(::mapEntityToClass)
        )
    }

    private suspend fun saveTimetableInCache(timetable: Timetable<Owner>) {
        saveOwnerInCache(timetable.owner)
        val classEntities = timetable.classes.map(::mapClassToEntity)
        classEntities.forEach { timetableClassDao.insert(it) }
    }

    private fun sortTimetableClasses(classes: List<TimetableClass>): List<TimetableClass> {
        return classes.sortedWith(
            compareBy<TimetableClass> { Day.Companion.getById(it.day).orderIndex }
                .thenBy { it.startHour }
                .thenBy { it.endHour }
        )
    }

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
            groupId = timetableClass.groupId,
            ownerTypeId = timetableClass.ownerTypeId,
            subject = timetableClass.subject,
            teacher = timetableClass.teacher,
            isVisible = timetableClass.isVisible,
            configurationId = timetableClass.configurationId
        )
    }

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
            groupId = entity.groupId,
            ownerTypeId = entity.ownerTypeId,
            subject = entity.subject,
            teacher = entity.teacher,
            isVisible = entity.isVisible,
            configurationId = entity.configurationId,
        )
    }

    protected abstract suspend fun getOwnersFromCache(
        configurationId: String,
    ): List<Owner>

    protected abstract suspend fun saveOwnerInCache(
        owner: Owner,
    )

    protected abstract suspend fun getOwnersFromApi(
        year: Int,
        semesterId: String,
    ): Resource<List<Owner>>

    protected abstract suspend fun getTimetableFromApi(
        year: Int,
        semesterId: String,
        owner: Owner,
    ): Resource<Timetable<Owner>>

    protected abstract fun sortOwners(
        owners: List<Owner>,
    ): List<Owner>
}