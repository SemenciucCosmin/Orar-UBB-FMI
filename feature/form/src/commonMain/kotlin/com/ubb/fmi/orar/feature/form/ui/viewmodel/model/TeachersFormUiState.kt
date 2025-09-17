package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import com.ubb.fmi.orar.ui.catalog.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

/**
 * UiState for teacher selection screen for timetable configuration
 * @param teachers: list of teachers for list display
 * @param selectedFilterId: id of selected teacher title filter
 * @param selectedTeacherId: id of selected teacher
 * @param isLoading: boolean for loading state
 * @param errorStatus: error state
 */
data class TeachersFormUiState(
    private val teachers: ImmutableList<TimetableOwner.Teacher> = persistentListOf(),
    val selectedFilterId: String = TeacherTitleFilter.ALL.id,
    val selectedTeacherId: String? = null,
    val isLoading: Boolean = false,
    val errorStatus: ErrorStatus? = null,
) {
    enum class TeachersFormEvent : Event {
        CONFIGURATION_DONE
    }

    companion object {
        /**
         * Filtered teachers by selected teacehr title filter
         */
        val TeachersFormUiState.filteredTeachers: ImmutableList<TimetableOwner.Teacher>
            get() {
                return teachers.filter { teacher ->
                    selectedFilterId in listOf(
                        teacher.titleId,
                        TeacherTitleFilter.ALL.id
                    )
                }.toImmutableList()
            }
    }
}
