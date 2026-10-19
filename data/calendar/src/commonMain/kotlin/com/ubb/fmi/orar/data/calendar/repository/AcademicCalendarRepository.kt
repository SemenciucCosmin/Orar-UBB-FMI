package com.ubb.fmi.orar.data.calendar.repository

import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.network.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Repository for managing data flow and data source for the academic calendar
 */
interface AcademicCalendarRepository {

    /**
     * Retrieves a [Flow] of the academic calendar
     */
    fun getCalendar(): Flow<Resource<AcademicCalendar>>

    /**
     * Invalidates academic calendar cache for [year]
     */
    suspend fun invalidate(year: Int)
}
