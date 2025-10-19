package com.ubb.fmi.orar.domain.usertimetable.usecase

import com.ubb.fmi.orar.data.timetable.datasource.EventsDataSource
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import com.ubb.fmi.orar.domain.extensions.PIPE
import kotlinx.coroutines.flow.firstOrNull
import okio.ByteString.Companion.encodeUtf8

/**
 * Creates a copy in database of an event under the [Owner.User]
 * This allows the user to introduce foreign events into its timetable
 */
class AdoptEventUseCase(
    private val timetablePreferences: TimetablePreferences,
    private val eventsDataSource: EventsDataSource,
) {
    suspend operator fun invoke(eventId: String) {
        val configuration = timetablePreferences.getConfiguration().firstOrNull() ?: return
        val configurationId = configuration.year.toString() + configuration.semesterId
        val event = eventsDataSource.getEventFromCache(configurationId, eventId) ?: return
        val id = listOf(
            event.id,
            Owner.User.id
        ).joinToString(String.PIPE).encodeUtf8().sha256().hex()

        val adoptedEvent = event.copy(id = id)
        eventsDataSource.saveEventInCache(Owner.User.id, adoptedEvent)
    }
}