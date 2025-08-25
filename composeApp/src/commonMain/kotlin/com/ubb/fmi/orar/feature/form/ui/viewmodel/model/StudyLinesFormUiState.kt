package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.feature.studyLines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event

data class StudyLinesFormUiState(
    private val groupedStudyLines: List<List<StudyLine>> = emptyList(),
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
        val StudyLinesFormUiState.filteredGroupedStudyLines: List<List<StudyLine>>
            get() {
                return groupedStudyLines.filter { studyLines ->
                    studyLines.all {
                        it.degreeId == selectedFilter.id
                    } || selectedFilter == DegreeFilter.ALL
                }
            }
    }
}

val StudyLinesFormUiState.isNextEnabled: Boolean
    get() = selectedStudyLineBaseId != null && selectedStudyYearId != null
