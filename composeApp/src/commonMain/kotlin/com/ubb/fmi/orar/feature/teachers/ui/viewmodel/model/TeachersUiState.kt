package com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model

import com.ubb.fmi.orar.data.teachers.model.Teacher

data class TeachersUiState(
    private val teachers: List<Teacher> = emptyList(),
    val selectedFilter: TeacherTitleFilter = TeacherTitleFilter.ALL,
    val isLoading: Boolean = true,
    val isError: Boolean = true
) {
    companion object {
        val TeachersUiState.filteredTeachers: List<Teacher>
            get() {
                return teachers.filter { teacher ->
                    teacher.titleId == selectedFilter.id || selectedFilter == TeacherTitleFilter.ALL
                }
            }
    }
}

