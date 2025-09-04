package com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.ui.catalog.model.DegreeFilter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Ui state for the Study Lines screen.
 * It holds the grouped study lines, selected filter and field IDs, loading and error states.
 * This state is used to display study lines grouped by fields,
 * allowing users to filter by degree and select specific fields.
 * @property groupedStudyLines A list of lists containing study lines grouped by fields.
 * @property selectedFilterId The ID of the currently selected degree filter.
 * @property selectedFieldId The ID of the currently selected field, or null if none is selected.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property isError Indicates whether there was an error loading the data.
 */
data class StudyLinesUiState(
    private val groupedStudyLines: ImmutableList<ImmutableList<TimetableOwner.StudyLine>> = persistentListOf(),
    val selectedFilterId: String = DegreeFilter.ALL.id,
    val selectedFieldId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        /**
         * Extension property to get the filtered grouped study lines based on the selected filter ID.
         * It filters the grouped study lines to include only those that match the selected degree filter.
         */
        val StudyLinesUiState.filteredGroupedStudyLines: ImmutableList<ImmutableList<TimetableOwner.StudyLine>>
            get() {
                return groupedStudyLines.filter { studyLines ->
                    studyLines.all {
                        it.degreeId == selectedFilterId
                    } || selectedFilterId == DegreeFilter.ALL.id
                }.toImmutableList()
            }
    }
}
