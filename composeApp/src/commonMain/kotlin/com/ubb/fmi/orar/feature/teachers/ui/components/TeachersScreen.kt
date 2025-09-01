package com.ubb.fmi.orar.feature.teachers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.teachers.ui.model.TeacherTitle
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState.Companion.filteredTeachers
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.stringResource

@Composable
fun TeachersScreen(
    uiState: TeachersUiState,
    onTeacherClick: (String) -> Unit,
    onSelectFilter: (TeacherTitleFilter) -> Unit,
    onRetryClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    Scaffold(bottomBar = bottomBar) { paddingValues ->
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
                        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
                        contentPadding = PaddingValues(horizontal = Pds.spacing.Medium)
                    ) {
                        items(
                            TeacherTitleFilter.entries.sortedBy { it.orderIndex }
                        ) { teacherTitleFilter ->
                            FilterChip(
                                shape = CircleShape,
                                selected = uiState.selectedFilter == teacherTitleFilter,
                                onClick = { onSelectFilter(teacherTitleFilter) },
                                label = {
                                    Text(
                                        text = stringResource(teacherTitleFilter.labelRes)
                                    )
                                }
                            )
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(Pds.spacing.Medium),
                        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                    ) {
                        items(uiState.filteredTeachers) { teacher ->
                            TeacherListItem(
                                name = teacher.name,
                                onClick = { onTeacherClick(teacher.id) },
                                title = stringResource(
                                    TeacherTitle.getById(teacher.titleId).labelRes
                                ),
                            )
                        }
                    }
                }
            }
        }
    }
}