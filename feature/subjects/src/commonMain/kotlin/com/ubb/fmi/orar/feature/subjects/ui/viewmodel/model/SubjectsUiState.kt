package com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * Represents the UI state for the Subjects screen.
 * It contains a list of subjects, a search query, and flags for loading and error states.
 * The `filteredSubjects` property provides a list of subjects filtered by the search query.
 * @property subjects The list of subjects to display.
 * @property searchQuery The current search query entered by the user.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property isError Indicates whether there was an error loading the data.
 */
data class SubjectsUiState(
    private val subjects: ImmutableList<TimetableOwner.Subject> = persistentListOf(),
    val searchQuery: String = String.BLANK,
    val isLoading: Boolean = true,
    val isError: Boolean = true
) {
    companion object {
        /**
         * Creates an empty [SubjectsUiState] with no subjects, an empty search query,
         * and both loading and error states set to true.
         */
        val SubjectsUiState.filteredSubjects: ImmutableList<TimetableOwner.Subject>
            get() {
                return subjects.filter { subject ->
                    searchQuery.isBlank() || subject.name.lowercase().contains(searchQuery)
                }.toImmutableList()
            }
    }
}
