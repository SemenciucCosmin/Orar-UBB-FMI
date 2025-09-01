package com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model

import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

data class TeachersUiState(
    private val teachers: ImmutableList<TimetableOwner.Teacher> = persistentListOf(),
    val selectedFilterId: String = TeacherTitleFilter.ALL.id,
    val isLoading: Boolean = true,
    val isError: Boolean = true,
) {
    companion object {
        val TeachersUiState.filteredTeachers: ImmutableList<TimetableOwner.Teacher>
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

