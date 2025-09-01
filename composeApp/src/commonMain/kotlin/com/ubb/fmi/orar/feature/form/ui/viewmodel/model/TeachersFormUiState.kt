package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.viewmodel.model.Event
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class TeachersFormUiState(
    private val teachers: ImmutableList<TimetableOwner.Teacher> = persistentListOf(),
    val selectedFilter: TeacherTitleFilter = TeacherTitleFilter.ALL,
    val selectedTeacherId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    enum class TeachersFormEvent : Event {
        CONFIGURATION_DONE
    }

    companion object {
        val TeachersFormUiState.filteredTeachers: ImmutableList<TimetableOwner.Teacher>
            get() {
                return teachers.filter { teacher ->
                    teacher.titleId == selectedFilter.id || selectedFilter == TeacherTitleFilter.ALL
                }.toImmutableList()
            }
    }
}
