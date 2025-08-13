package com.ubb.fmi.orar.feature.form.viewmodel.model

import com.ubb.fmi.orar.data.studyline.model.StudyLine
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event

data class StudyLinesFormUiState(
    val studyLinesGroups: List<List<StudyLine>> = emptyList(),
    val selectedStudyLineBaseId: String? = null,
    val selectedStudyYearId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    enum class StudyLinesFormEvent: Event {
        SELECTION_DONE
    }
}

val StudyLinesFormUiState.isNextEnabled: Boolean
    get() = selectedStudyLineBaseId != null && selectedStudyYearId != null
