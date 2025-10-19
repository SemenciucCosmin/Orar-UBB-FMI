package com.ubb.fmi.orar.domain.usertimetable.usecase

import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Day
import com.ubb.fmi.orar.data.timetable.model.Event
import com.ubb.fmi.orar.data.timetable.model.EventType
import com.ubb.fmi.orar.data.timetable.model.Frequency
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.extensions.PIPE
import kotlinx.coroutines.flow.firstOrNull
import okio.ByteString.Companion.encodeUtf8

class AddPersonalEventUseCase(
    private val timetablePreferences: TimetablePreferences,
    private val eventsDataSource: EventsDataSource,
) {
    suspend operator fun invoke(
        activity: String,
        location: String,
        caption: String,
        details: String,
        startHour: Int,
        startMinute: Int,
        endHour: Int,
        endMinute: Int,
        frequency: Frequency,
        days: List<Day>,
    ) {
        val configuration = timetablePreferences.getConfiguration().firstOrNull() ?: return
        val configurationId = configuration.year.toString() + configuration.semesterId
        val personalEvents = days.map { day ->
            val id = listOf(
                activity,
                location,
                caption,
                details,
                startHour,
                startMinute,
                endHour,
                endMinute,
                frequency.id,
                day.id
            ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

            Event(
                id = id,
                configurationId = configurationId,
                day = day,
                frequency = frequency,
                startHour = startHour,
                startMinute = startMinute,
                endHour = endHour,
                endMinute = endMinute,
                location = location,
                activity = activity,
                type = EventType.PERSONAL,
                participant = String.BLANK,
                caption = caption,
                details = details,
                isVisible = true
            )
        }

        eventsDataSource.saveEventsInCache(
            ownerId = Owner.User.id,
            events = personalEvents
        )
    }
}