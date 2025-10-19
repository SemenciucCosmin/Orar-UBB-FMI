package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.domain.timetable.model.Semester
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.UiEvent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * UiState for teacher selection screen for timetable configuration
 * @param teachers: list of teachers for list display
 * @param selectedFilterId: id of selected teacher title filter
 * @param selectedTeacherId: id of selected teacher
 * @property searchQuery The current search query entered by the user.
 * @param isLoading: boolean for loading state
 * @param errorStatus: error state
 */
data class TeachersFormUiState(
    private val teachers: ImmutableList<Owner.Teacher> = persistentListOf(),
    val selectedFilterId: String = TeacherTitleFilter.ALL.id,
    val selectedTeacherId: String? = null,
    val searchQuery: String = String.BLANK,
    val year: Int? = null,
    val semester: Semester? = null,
    val isLoading: Boolean = false,
    val errorStatus: ErrorStatus? = null,
) {
    enum class TeachersFormUiEvent : UiEvent {
        CONFIGURATION_DONE
    }

    companion object {
        /**
         * Filtered teachers by selected teacher title filter
         */
        val TeachersFormUiState.filteredTeachers: ImmutableList<Owner.Teacher>
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
