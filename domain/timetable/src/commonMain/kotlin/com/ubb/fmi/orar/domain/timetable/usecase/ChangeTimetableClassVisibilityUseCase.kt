package com.ubb.fmi.orar.domain.timetable.usecase

import com.ubb.fmi.orar.data.timetable.datasource.TimetableClassDataSource

/**
 * Use case for changing the visibility of a timetable class based on its owner type.
 * This use case interacts with various data sources to perform the visibility change.
 *
 * @property timetableClassDataSource The data source for timetable class related operations.
 */
class ChangeTimetableClassVisibilityUseCase(
    private val timetableClassDataSource: TimetableClassDataSource,
) {
    /**
     * Changes the visibility of a timetable class based on its ID and owner type.
     *
     * @param timetableClassId The ID of the timetable class to change visibility for.
     */
    suspend operator fun invoke(timetableClassId: String) {
        timetableClassDataSource.changeTimetableClassVisibility(timetableClassId)
    }
}
