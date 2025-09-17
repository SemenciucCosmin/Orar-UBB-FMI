package com.ubb.fmi.orar.feature.teachers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.feature.teachers.ui.model.TeacherTitle
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState
import com.ubb.fmi.orar.feature.teachers.ui.viewmodel.model.TeachersUiState.Companion.filteredTeachers
import com.ubb.fmi.orar.ui.catalog.components.list.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.catalog.model.TeacherTitleFilter
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.feature.teachers.generated.resources.Res
import orar_ubb_fmi.feature.teachers.generated.resources.ic_teacher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * TeachersScreen is a composable function that displays a list of teachers with filtering options.
 * It includes a chip selection row for filtering teachers by title and a lazy column to display the list of teachers.
 * It also handles loading and error states with a scaffold layout.
 * @param uiState The current state of the teachers UI, containing the list of teachers and filter options.
 * @param onTeacherClick Callback function to handle teacher item clicks, providing the teacher's ID.
 * @param onSelectFilter Callback function to handle filter selection, providing the selected filter ID.
 * @param onRetryClick Callback function to handle retry actions when an error occurs.
 * @param bottomBar A composable function for the bottom bar, which can be used to
 */
@Composable
fun TeachersScreen(
    uiState: TeachersUiState,
    onTeacherClick: (String) -> Unit,
    onSelectFilter: (String) -> Unit,
    onRetryClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        bottomBar = bottomBar
    ) { paddingValues ->
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

@Preview
@Composable
private fun PreviewTeachersScreen() {
    OrarUbbFmiTheme {
        TeachersScreen(
            onTeacherClick = {},
            onSelectFilter = {},
            onRetryClick = {},
            bottomBar = {},
            uiState = TeachersUiState(
                selectedFilterId = "Licenta",
                isLoading = false,
                errorStatus = null,
                teachers = List(5) {
                    TimetableOwner.Teacher(
                        id = it.toString(),
                        name = "Teacher $it",
                        configurationId = "20241",
                        titleId = "Prof.",
                    )
                }.toImmutableList()
            )
        )
    }
}
