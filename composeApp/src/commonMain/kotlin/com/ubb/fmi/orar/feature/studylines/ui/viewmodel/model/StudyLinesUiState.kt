package com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model

import com.ubb.fmi.orar.data.studyline.model.StudyLine

data class StudyLinesUiState(
    private val groupedStudyLines: List<List<StudyLine>> = emptyList(),
    val selectedFilter: DegreeFilter = DegreeFilter.ALL,
    val selectedStudyLineBaseId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val StudyLinesUiState.filteredGroupedStudyLines: List<List<StudyLine>>
            get() {
                return groupedStudyLines.filter { studyLines ->
                    studyLines.all {
                        it.degreeId == selectedFilter.id
                    } || selectedFilter == DegreeFilter.ALL
                }
            }
    }
}
