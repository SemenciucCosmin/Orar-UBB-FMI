package com.ubb.fmi.orar.feature.form.ui.viewmodel.model

import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter

data class TeachersFormUiState(
    private val teachers: List<Teacher> = emptyList(),
    val selectedFilter: TeacherTitleFilter = TeacherTitleFilter.ALL,
    val selectedTeacherId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
) {
    companion object {
        val TeachersFormUiState.filteredTeachers: List<Teacher>
            get() {
                return teachers.filter { teacher ->
                    teacher.titleId == selectedFilter.id || selectedFilter == TeacherTitleFilter.ALL
                }
            }
    }
}
