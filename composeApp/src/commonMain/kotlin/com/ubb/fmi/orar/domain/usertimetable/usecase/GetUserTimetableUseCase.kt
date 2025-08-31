package com.ubb.fmi.orar.domain.usertimetable.usecase

import com.ubb.fmi.orar.data.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.ui.catalog.model.UserType
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.timetable.model.Timetable
import com.ubb.fmi.orar.data.groups.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.teachers.datasource.TeachersDataSource
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest

@Suppress("UNCHECKED_CAST")
class GetUserTimetableUseCase(
    private val studyLinesDataSource: StudyLinesDataSource,
    private val teachersDataSource: TeachersDataSource,
    private val timetablePreferences: TimetablePreferences,
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<Resource<Timetable<TimetableOwner>>> {
        return timetablePreferences.getConfiguration().mapLatest { configuration ->
            if (configuration == null) {
                return@mapLatest Resource(null, Status.Error)
            }

            when (UserType.getById(configuration.userTypeId)) {
                UserType.STUDENT -> {
                    val studyLevel = configuration.studyLevelId?.let { StudyLevel.getById(it) }
                    val fieldId = configuration.fieldId
                    val groupId = configuration.groupId

                    if (studyLevel == null || fieldId == null || groupId == null) {
                        return@mapLatest Resource(null, Status.Error)
                    }

                    return@mapLatest studyLinesDataSource.getTimetable(
                        year = configuration.year,
                        semesterId = configuration.semesterId,
                        ownerId = fieldId + studyLevel.notation,
                        groupId = groupId,
                    ) as Resource<Timetable<TimetableOwner>>
                }

                UserType.TEACHER -> {
                    if (configuration.teacherId == null) {
                        return@mapLatest  Resource(null, Status.Error)
                    }

                    return@mapLatest  teachersDataSource.getTimetable(
                        year = configuration.year,
                        semesterId = configuration.semesterId,
                        ownerId = configuration.teacherId,
                    ) as Resource<Timetable<TimetableOwner>>
                }
            }
        }
    }
}