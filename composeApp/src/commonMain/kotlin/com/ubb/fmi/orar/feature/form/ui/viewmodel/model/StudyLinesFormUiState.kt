package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.form.ui.model.Semester
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class StudyLinesFormUiState(
    private val groupedStudyLines: ImmutableList<ImmutableList<TimetableOwner.StudyLine>> = persistentListOf(),
    val selectedFilter: DegreeFilter = DegreeFilter.ALL,
    val selectedFieldId: String? = null,
    val selectedStudyLevelId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val StudyLinesFormUiState.filteredGroupedStudyLines: ImmutableList<ImmutableList<TimetableOwner.StudyLine>>
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
    get() = selectedFieldId != null && selectedStudyLevelId != null
