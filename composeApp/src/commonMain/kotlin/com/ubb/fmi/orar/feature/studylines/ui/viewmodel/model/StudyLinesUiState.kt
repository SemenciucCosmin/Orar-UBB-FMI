package com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model

import com.ubb.fmi.orar.data.studyline.model.StudyLine
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class StudyLinesUiState(
    private val groupedStudyLines: ImmutableList<ImmutableList<StudyLine>> = persistentListOf(),
    val selectedFilter: DegreeFilter = DegreeFilter.ALL,
    val selectedStudyLineBaseId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val StudyLinesUiState.filteredGroupedStudyLines: ImmutableList<ImmutableList<StudyLine>>
            get() {
                return groupedStudyLines.filter { studyLines ->
                    studyLines.all {
                        it.degreeId == selectedFilter.id
                    } || selectedFilter == DegreeFilter.ALL
                }.toImmutableList()
            }
    }
}
