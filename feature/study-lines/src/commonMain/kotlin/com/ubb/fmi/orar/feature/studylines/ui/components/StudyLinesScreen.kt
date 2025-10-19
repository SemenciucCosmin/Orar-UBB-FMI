package com.ubb.fmi.orar.feature.studylines.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Degree
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.ui.catalog.components.list.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemExpandable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.catalog.model.DegreeFilter
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_study_line
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays the Study Lines screen.
 * It shows a list of study lines grouped by fields, with options to filter and select study levels.
 * @param uiState The current state of the study lines UI.
 * @param onStudyLineClick Callback invoked when a study line is clicked, passing the field ID.
 * @param onStudyLevelClick Callback invoked when a study level is clicked, passing the level ID.
 * @param onSelectFilter Callback invoked when a filter is selected, passing the filter ID.
 * @param onRetryClick Callback invoked when the user wants to retry loading data after an error.
 * @param bottomBar Composable function for the bottom bar of the screen.
 */
@Composable
fun StudyLinesScreen(
    uiState: StudyLinesUiState,
    onStudyLineClick: (String) -> Unit,
    onStudyLevelClick: (String) -> Unit,
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
                chips = DegreeFilter.entries.sortedBy { it.orderIndex }.map {
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
                items(uiState.filteredGroupedStudyLines) { groupedStudyLines ->
                    val fieldId = groupedStudyLines.firstOrNull()?.fieldId ?: return@items
                    val label = groupedStudyLines.firstOrNull()?.name ?: return@items

                    ListItemExpandable(
                        headline = label,
                        isSelected = uiState.selectedFieldId == fieldId,
                        onClick = { onStudyLineClick(fieldId) },
                        leadingIcon = painterResource(Res.drawable.ic_study_line),
                        expandedContent = {
                            groupedStudyLines.forEach {
                                ListItemClickable(
                                    headline = stringResource(it.level.labelRes),
                                    onClick = { onStudyLevelClick(it.level.id) },
                                    trailingIconSize = Pds.icon.Small,
                                    headlineTextStyle = MaterialTheme.typography.bodySmall
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewStudyLinesScreen() {
    OrarUbbFmiTheme {
        StudyLinesScreen(
            onStudyLineClick = {},
            onStudyLevelClick = {},
            onSelectFilter = {},
            onRetryClick = {},
            bottomBar = {},
            uiState = StudyLinesUiState(
                isLoading = false,
                errorStatus = null,
                selectedFilterId = "Licenta",
                selectedFieldId = "IE",
                groupedStudyLines = List(2) {
                    List(2) {
                        StudyLine(
                            id = "IE1",
                            name = "Informatica Engleza",
                            configurationId = "20241",
                            fieldId = "IE",
                            level = StudyLevel.LEVEL_1,
                            degree = Degree.LICENCE
                        )
                    }.toImmutableList()
                }.toImmutableList(),
            )
        )
    }
}
