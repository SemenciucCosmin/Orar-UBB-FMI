package com.ubb.fmi.orar.feature.teachers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.teachers.ui.model.TeacherTitle
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState.Companion.filteredTeachers
import com.ubb.fmi.orar.ui.catalog.components.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_teacher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun TeachersScreen(
    uiState: TeachersUiState,
    onTeacherClick: (String) -> Unit,
    onSelectFilter: (String) -> Unit,
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
                    ChipSelectionRow(
                        selectedChipId = uiState.selectedFilterId,
                        onClick = onSelectFilter,
                        contentPadding = PaddingValues(horizontal = Pds.spacing.Medium),
                        chips = TeacherTitleFilter.entries.sortedBy { it.orderIndex }.map {
                            Chip(
                                id = it.id,
                                label = stringResource(it.labelRes)
                            )
                        }
                    )

                    LazyColumn(
                        contentPadding = PaddingValues(Pds.spacing.Medium),
                        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                    ) {
                        items(uiState.filteredTeachers) { teacher ->
                            val teacherTitle = TeacherTitle.getById(teacher.titleId)

                            ListItemClickable(
                                headline = teacher.name,
                                overline = stringResource(teacherTitle.labelRes),
                                onClick = { onTeacherClick(teacher.id) },
                                leadingIcon = painterResource(Res.drawable.ic_teacher),
                            )
                        }
                    }
                }
            }
        }
    }
}