package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.settings.preferences.SettingsPreferences
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.model.UserType
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

class InitializeTimetableNotificationsUseCase(
    private val coroutineScope: CoroutineScope,
    private val groupsRepository: GroupsRepository,
    private val teacherRepository: TeacherRepository,
    private val eventsDataSource: EventsDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val settingsPreferences: SettingsPreferences,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val invalidateTimetableNotificationsUseCase: InvalidateTimetableNotificationsUseCase,
    private val logger: Logger,
) {
    operator fun invoke() {
        coroutineScope.launch {
            timetablePreferences.getConfiguration()
                .filterNotNull()
                .drop(if (settingsPreferences.getNotificationsInitialSetup()) 0 else 0)
                .collectLatest { configuration ->
                    settingsPreferences.setNotificationsInitialSetup(true)
                    invalidateTimetableNotificationsUseCase()

                    logger.d(TAG, "configuration $configuration")

                    val impersonalEvents = when (UserType.getById(configuration.userTypeId)) {
                        UserType.STUDENT -> {
                            val studyLevel = configuration.studyLevelId?.let {
                                StudyLevel.getById(it)
                            }

                            val fieldId = configuration.fieldId
                            val groupId = configuration.groupId
                            val studyLineId = studyLevel?.notation?.let { fieldId + it }

                            if (studyLineId == null || groupId == null) return@collectLatest
                            groupsRepository.getTimetable(groupId, studyLineId).map {
                                it.payload?.events
                            }
                        }

                        UserType.TEACHER -> {
                            val teacherId = configuration.teacherId ?: return@collectLatest
                            teacherRepository.getTimetable(teacherId).map {
                                it.payload?.events
                            }
                        }
                    }.filterNotNull().firstOrNull() ?: return@collectLatest

                    val initializedEventsNotifications = impersonalEvents.map {
                        it.copy(isNotificationOn = true)
                    }

                    val groupedEvents = initializedEventsNotifications.groupBy { it.ownerId }
                    groupedEvents.forEach { (ownerId, events) ->
                        eventsDataSource.saveEventsInCache(
                            ownerId = ownerId,
                            events = events
                        )
                    }

                    scheduleEventNotificationsUseCase(
                        *initializedEventsNotifications.toTypedArray()
                    )
                }
        }
    }

    companion object {
        private const val TAG = "SetupEventsNotificationUseCase"
    }
}
