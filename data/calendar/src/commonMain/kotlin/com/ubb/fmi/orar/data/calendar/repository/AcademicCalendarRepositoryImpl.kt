package com.ubb.fmi.orar.data.calendar.repository

import com.ubb.fmi.orar.data.calendar.datasource.AcademicCalendarDataSource
import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Repository for managing data flow and data source for the academic calendar
 */
class AcademicCalendarRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val academicCalendarDataSource: AcademicCalendarDataSource,
    private val timetablePreferences: TimetablePreferences,
) : AcademicCalendarRepository {

    private val calendarFlow: MutableStateFlow<Resource<AcademicCalendar>> = MutableStateFlow(
        Resource(null, Status.Loading)
    )

    init {
        prefetchCalendar()
        initializeCalendar()
    }

    /**
     * Retrieves a [Flow] of the academic calendar
     */
    override fun getCalendar(): Flow<Resource<AcademicCalendar>> {
        return calendarFlow
    }

    /**
     * Fetches the academic calendar from API again, to recover from a failed fetch
     */
    override fun refresh() {
        calendarFlow.update { Resource(it.payload, Status.Loading) }
        coroutineScope.launch {
            when (val configuration = timetablePreferences.getConfiguration().firstOrNull()) {
                null -> calendarFlow.update { Resource(null, Status.NotFoundError) }
                else -> getCalendarFromApi(configuration.year)
            }
        }
    }

    /**
     * Invalidates academic calendar cache for [year]
     */
    override suspend fun invalidate(year: Int) {
        academicCalendarDataSource.invalidate(year)
    }

    /**
     * Tries prefetching the academic calendar from API for a safety update of local data
     */
    private fun prefetchCalendar() {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let { getCalendarFromApi(it.year) }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
    private fun initializeCalendar() {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) return@collectLatest
                getCalendarFromCache(configuration.year)
            }
        }
    }

    /**
     * Provides the collection of database data flow
     */
    private suspend fun getCalendarFromCache(year: Int) {
        academicCalendarDataSource.getCalendarFromCache(year).collectLatest { academicCalendar ->
            when {
                academicCalendar == null -> getCalendarFromApi(year)
                else -> calendarFlow.update { Resource(academicCalendar, Status.Success) }
            }
        }
    }

    /**
     * Retrieves the academic calendar from API and update the database or output flow.
     * The API only provides the current academic calendar, so a calendar of another [year]
     * is still cached but reported as not found for the requested [year].
     */
    private suspend fun getCalendarFromApi(year: Int) {
        val resource = academicCalendarDataSource.getCalendarFromApi()
        val academicCalendar = resource.payload

        if (resource.status.isSuccess() && academicCalendar != null) {
            academicCalendarDataSource.saveCalendarInCache(academicCalendar)
            if (academicCalendar.startYear == year) {
                calendarFlow.update {
                    when (it.payload) {
                        null -> it
                        else -> it.copy(status = Status.Success)
                    }
                }
                return
            }
        }

        val status = when {
            resource.status.isSuccess() -> Status.NotFoundError
            else -> resource.status
        }

        calendarFlow.update {
            when (it.payload) {
                null -> Resource(null, status)
                else -> it.copy(status = Status.Success)
            }
        }
    }
}
