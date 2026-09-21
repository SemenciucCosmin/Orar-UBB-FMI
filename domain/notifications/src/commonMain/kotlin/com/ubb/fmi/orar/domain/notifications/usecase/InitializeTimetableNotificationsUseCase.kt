package com.ubb.fmi.orar.domain.notifications.usecase

import Logger
import com.ubb.fmi.orar.data.groups.repository.GroupsRepository
import com.ubb.fmi.orar.data.teachers.repository.TeacherRepository
import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.model.UserType
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.collections.component1
import kotlin.collections.component2

/**
 * Re-syncs notifications from the network: invalidates all currently scheduled notifications,
 * fetches the user's current timetable, force-enables notifications for every visible
 * non-personal event, and schedules them.
 *
 * This always turns notifications *on* for every visible event, overriding any per-event
 * choice the user previously made. It's meant for first-time setup (e.g. when notification
 * permission is newly granted), not for periodic refreshes — use
 * [RescheduleNotificationsUseCase] instead when you only want to restore the
 * notifications the user already had enabled.
 */
class InitializeTimetableNotificationsUseCase(
    private val coroutineScope: CoroutineScope,
    private val groupsRepository: GroupsRepository,
    private val teacherRepository: TeacherRepository,
    private val eventsDataSource: EventsDataSource,
    private val timetablePreferences: TimetablePreferences,
    private val scheduleEventNotificationsUseCase: ScheduleEventNotificationsUseCase,
    private val invalidateTimetableNotificationsUseCase: InvalidateTimetableNotificationsUseCase,
    private val logger: Logger,
) {
    operator fun invoke() {
        coroutineScope.launch {
            val configuration = timetablePreferences
                .getConfiguration()
                .firstOrNull() ?: return@launch

            invalidateTimetableNotificationsUseCase()
            logger.d(TAG, "configuration $configuration")

            val impersonalEvents = when (UserType.getById(configuration.userTypeId)) {
                UserType.STUDENT -> {
                    val studyLevel = configuration.studyLevelId?.let { StudyLevel.getById(it) }
                    val fieldId = configuration.fieldId
                    val groupId = configuration.groupId
                    val studyLineId = studyLevel?.notation?.let { fieldId + it }

                    if (studyLineId == null || groupId == null) return@launch
                    groupsRepository.getTimetable(groupId, studyLineId).map {
                        it.payload?.events
                    }
                }

                UserType.TEACHER -> {
                    val teacherId = configuration.teacherId ?: return@launch
                    teacherRepository.getTimetable(teacherId).map {
                        it.payload?.events
                    }
                }
            }.filterNotNull().firstOrNull() ?: return@launch

            val visibleImpersonalEvents = impersonalEvents.filter { it.isVisible }
            val initializedEventsNotifications = visibleImpersonalEvents.map {
                it.copy(isNotificationOn = true)
            }

            initializedEventsNotifications.groupBy { it.ownerId }.forEach { (ownerId, events) ->
                eventsDataSource.saveEventsInCache(
                    ownerId = ownerId,
                    events = events
                )
            }

            scheduleEventNotificationsUseCase(*initializedEventsNotifications.toTypedArray())
        }
    }

    companion object {
        private const val TAG = "SetupEventsNotificationUseCase"
    }
}
