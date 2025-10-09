package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.ui.catalog.model.DegreeFilter
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * UiState for study line selection screen for timetable configuration
 * @param groupedStudyLines: list of study lines grouped by field for list display
 * @param selectedFilterId: id of selected degree filter
 * @param selectedFieldId: id of selected study line field
 * @param isLoading: boolean for loading state
 * @param errorStatus: error state
 */
data class StudyLinesFormUiState(
    private val groupedStudyLines: ImmutableList<ImmutableList<StudyLine>> = persistentListOf(),
    val selectedFilterId: String = DegreeFilter.ALL.id,
    val selectedFieldId: String? = null,
    val selectedStudyLevelId: String? = null,
    val isLoading: Boolean = false,
    val errorStatus: ErrorStatus? = null,
) {
    companion object {
        /**
         * Filtered grouped study lines by selected degree filter
         */
        val StudyLinesFormUiState.filteredGroupedStudyLines: ImmutableList<ImmutableList<StudyLine>>
            get() {
                return groupedStudyLines.filter { studyLines ->
                    studyLines.all {
                        it.degreeId == selectedFilterId
                    } || selectedFilterId == DegreeFilter.ALL.id
                }.toImmutableList()
            }
    }
}

/**
 * Computed value for specific button on study lines form screen
 */
val StudyLinesFormUiState.isNextEnabled: Boolean
    get() = selectedFieldId != null && selectedStudyLevelId != null
