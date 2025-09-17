package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.TeachersFormUiState.Companion.filteredTeachers
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemSelectable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.catalog.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.feature.form.generated.resources.Res
import orar_ubb_fmi.feature.form.generated.resources.lbl_next
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable screen with teachers that are selectable for timetable configuration
 * @param title: screen title
 * @param uiState: screen ui state
 * @param onTeacherClick: lambda for teacher selection
 * @param onSelectFilter: lambda for filter selection
 * @param onNextClick: lambda for next button action
 * @param onRetryClick: lambda for retry button action
 * @param onBack: lambda for back button action
 */
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
    StateScaffold(
        isLoading = uiState.isLoading,
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        topBar = {
            if (!uiState.isLoading) {
                TopBar(
                    title = title,
                    onBack = onBack
                )
            }
        }
    ) { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            ChipSelectionRow(
                selectedChipId = uiState.selectedFilterId,
                onClick = onSelectFilter,
                contentPadding = PaddingValues(
                    horizontal = Pds.spacing.Medium,
                    vertical = Pds.spacing.Small,
                ),
                chips = TeacherTitleFilter.entries.sortedBy { it.orderIndex }.map {
                    Chip(
                        id = it.id,
                        label = stringResource(it.labelRes)
                    )
                }
            )

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                contentPadding = PaddingValues(horizontal = Pds.spacing.Medium)
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

            PrimaryButton(
                onClick = onNextClick,
                enabled = uiState.selectedTeacherId != null,
                text = stringResource(Res.string.lbl_next),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Pds.spacing.Medium)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewTeachersFormScreen() {
    OrarUbbFmiTheme {
        TeachersFormScreen(
            title = "2024-2025, Semester 1",
            onTeacherClick = {},
            onSelectFilter = {},
            onNextClick = {},
            onRetryClick = {},
            onBack = {},
            uiState = TeachersFormUiState(
                selectedFilterId = "Prof.",
                selectedTeacherId = "",
                isLoading = false,
                errorStatus = null,
                teachers = List(4) {
                    TimetableOwner.Teacher(
                        id = "",
                        name = "Teacher $it",
                        configurationId = "20241",
                        titleId = "Prof."
                    )
                }.toImmutableList()
            )
        )
    }
}
