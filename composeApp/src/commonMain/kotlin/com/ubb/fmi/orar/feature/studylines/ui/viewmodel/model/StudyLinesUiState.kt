package com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class StudyLinesUiState(
    private val groupedStudyLines: ImmutableList<ImmutableList<TimetableOwner.StudyLine>> = persistentListOf(),
    val selectedFilterId: String = DegreeFilter.ALL.id,
    val selectedFieldId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
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
