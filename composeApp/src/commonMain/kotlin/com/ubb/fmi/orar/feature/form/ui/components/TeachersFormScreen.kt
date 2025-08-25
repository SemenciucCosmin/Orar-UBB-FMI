package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState.Companion.filteredTeachers
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.FormListItem
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@Composable
fun TeachersFormScreen(
    uiState: TeachersFormUiState,
    onTeacherClick: (String) -> Unit,
    onSelectFilter: (TeacherTitleFilter) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold { paddingValues ->
        when {
            uiState.isLoading -> {
                ProgressOverlay(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            uiState.isError -> {
                FailureState(
                    onRetry = onRetryClick,
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                )
            }

            else -> {
                Column(modifier = Modifier.padding(paddingValues)) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(
                            TeacherTitleFilter.entries.sortedBy { it.orderIndex }
                        ) { teacherTitleFilter ->
                            FilterChip(
                                shape = CircleShape,
                                selected = uiState.selectedFilter == teacherTitleFilter,
                                onClick = { onSelectFilter(teacherTitleFilter) },
                                label = { Text(text = teacherTitleFilter.label) }
                            )
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.filteredTeachers) { teacher ->
                            FormListItem<String>(
                                modifier = Modifier.fillMaxWidth(),
                                headlineLabel = teacher.name,
                                overLineLabel = teacher.titleId,
                                isSelected = uiState.selectedTeacherId == teacher.id,
                                onClick = { onTeacherClick(teacher.id) }
                            )
                        }
                    }


                    Button(
                        onClick = onNextClick,
                        enabled = uiState.selectedTeacherId != null,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(text = "NEXT")
                    }
                }
            }
        }
    }
}
