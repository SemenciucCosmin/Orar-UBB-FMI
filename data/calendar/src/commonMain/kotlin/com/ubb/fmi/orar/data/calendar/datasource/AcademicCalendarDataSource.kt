package com.ubb.fmi.orar.data.calendar.datasource

import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.network.model.Resource
import kotlinx.coroutines.flow.Flow

/**
 * Data source for managing academic calendar related information
 */
interface AcademicCalendarDataSource {

    /**
     * Retrieves the academic calendar page HTML from API
     */
    suspend fun getCalendarFromApi(): Resource<AcademicCalendar>

    suspend fun getCalendarFromCache(year: Int): Flow<AcademicCalendar?>

    suspend fun saveCalendarInCache(academicCalendar: AcademicCalendar)

    suspend fun invalidate(year: Int)
}
