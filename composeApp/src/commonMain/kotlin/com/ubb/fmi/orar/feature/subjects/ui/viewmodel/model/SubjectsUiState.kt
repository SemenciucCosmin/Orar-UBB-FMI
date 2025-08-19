package com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model

import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.domain.extensions.BLANK

data class SubjectsUiState(
    private val subjects: List<Subject> = emptyList(),
    val searchQuery: String = String.BLANK,
    val isLoading: Boolean = true,
    val isError: Boolean = true
) {
    companion object {
        val SubjectsUiState.filteredSubjects: List<Subject>
            get() {
                return subjects.filter { subject ->
                    searchQuery.isBlank() || subject.name.lowercase().contains(searchQuery)
                }
            }
    }
}

