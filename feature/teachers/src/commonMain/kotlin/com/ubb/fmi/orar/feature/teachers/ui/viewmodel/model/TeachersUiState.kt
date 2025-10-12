package com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.TeacherTitleFilter
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * TeachersUiState represents the state of the teachers screen in the application.
 * It holds a list of teachers, the selected filter ID, loading and error states.
 * It provides a computed property to filter teachers based on the selected filter ID.
 * @property teachers The list of teachers to be displayed.
 * @property selectedFilterId The ID of the currently selected filter for teacher titles.
 * @property searchQuery The current search query entered by the user.
 * @property isLoading Indicates whether the data is currently being loaded.
 * @property errorStatus Indicates whether there was an error loading the data.
 */
data class TeachersUiState(
    private val teachers: ImmutableList<Owner.Teacher> = persistentListOf(),
    val selectedFilterId: String = TeacherTitleFilter.ALL.id,
    val searchQuery: String = String.BLANK,
    val isLoading: Boolean = true,
    val errorStatus: ErrorStatus? = null,
) {
    companion object {
        /**
         * A default instance of TeachersUiState with no teachers, selected filter set to ALL,
         * and both loading and error states set to false.
         */
        val TeachersUiState.filteredTeachers: ImmutableList<Owner.Teacher>
            get() {
                return teachers.filter { teacher ->
                    selectedFilterId in listOf(
                        teacher.title.id,
                        TeacherTitleFilter.ALL.id
                    )
                }.filter { teacher ->
                    val isMatching = teacher.name.lowercase().contains(searchQuery.lowercase())
                    searchQuery.isBlank() || isMatching
                }.toImmutableList()
            }
    }
}
