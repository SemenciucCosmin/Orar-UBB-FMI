package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState.Companion.filteredTeachers
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.catalog.components.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemSelectable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_next
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeachersFormScreen(
    title: String,
    uiState: TeachersFormUiState,
    onTeacherClick: (String) -> Unit,
    onSelectFilter: (String) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (!uiState.isLoading) {
                TopBar(
                    title = title,
                    onBack = onBack
                )
            }
        }
    ) { paddingValues ->
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
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.filteredTeachers) { teacher ->
                            ListItemSelectable(
                                headline = teacher.name,
                                overline = teacher.titleId,
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
                            .padding(Pds.spacing.Medium)
                    ) {
                        Text(text = stringResource(Res.string.lbl_next))
                    }
                }
            }
        }
    }
}
