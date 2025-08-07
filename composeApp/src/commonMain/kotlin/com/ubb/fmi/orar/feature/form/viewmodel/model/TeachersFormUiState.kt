package com.ubb.fmi.orar.feature.form.viewmodel.model

import com.ubb.fmi.orar.data.teachers.model.Teacher

data class TeachersFormUiState(
    val teachers: List<Teacher> = emptyList(),
    val selectedTeacherId: String? = null,
    val isLoading: Boolean = false,
    val isError: Boolean = false,
)
