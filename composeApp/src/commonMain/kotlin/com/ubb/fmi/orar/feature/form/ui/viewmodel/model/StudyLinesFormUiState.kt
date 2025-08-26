package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class StudyLinesFormUiState(
    private val groupedStudyLines: ImmutableList<ImmutableList<StudyLine>> = persistentListOf(),
    val selectedFilter: DegreeFilter = DegreeFilter.ALL,
    val selectedStudyLineBaseId: String? = null,
    val selectedStudyYearId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    enum class StudyLinesFormEvent: Event {
        SELECTION_DONE
    }

    companion object {
        val StudyLinesFormUiState.filteredGroupedStudyLines: ImmutableList<ImmutableList<StudyLine>>
            get() {
                return groupedStudyLines.filter { studyLines ->
                    studyLines.all {
                        it.degreeId == selectedFilter.id
                    } || selectedFilter == DegreeFilter.ALL
                }.toImmutableList()
            }
    }
}

val StudyLinesFormUiState.isNextEnabled: Boolean
    get() = selectedStudyLineBaseId != null && selectedStudyYearId != null
