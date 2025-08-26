package com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model

import com.ubb.fmi.orar.data.subjects.model.Subject
import com.ubb.fmi.orar.domain.extensions.BLANK
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class SubjectsUiState(
    private val subjects: ImmutableList<Subject> = persistentListOf(),
    val searchQuery: String = String.BLANK,
    val isLoading: Boolean = true,
    val isError: Boolean = true
) {
    companion object {
        val SubjectsUiState.filteredSubjects: ImmutableList<Subject>
            get() {
                return subjects.filter { subject ->
                    searchQuery.isBlank() || subject.name.lowercase().contains(searchQuery)
                }.toImmutableList()
            }
    }
}

