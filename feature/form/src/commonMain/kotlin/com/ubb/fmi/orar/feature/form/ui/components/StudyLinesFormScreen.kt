package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemExpandable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.catalog.model.DegreeFilter
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.feature.form.generated.resources.Res
import orar_ubb_fmi.feature.form.generated.resources.lbl_next
import org.jetbrains.compose.resources.stringResource

/**
 * Composable screen with study lines that are selectable for timetable configuration
 * @param title: screen title
 * @param uiState: screen ui state
 * @param onStudyLineClick: lambda for study line selection
 * @param onStudyLevelClick: lambda for study level selection
 * @param onSelectFilter: lambda for filter selection
 * @param onNextClick: lambda for next button action
 * @param onRetryClick: lambda for retry button action
 * @param onBack: lambda for back button action
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyLinesFormScreen(
    title: String,
    uiState: StudyLinesFormUiState,
    onStudyLineClick: (String) -> Unit,
    onStudyLevelClick: (String) -> Unit,
    onSelectFilter: (String) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
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
                        expandedContent = {
                            ChipSelectionRow(
                                selectedChipId = uiState.selectedStudyLevelId,
                                shape = MaterialTheme.shapes.small,
                                onClick = onStudyLevelClick,
                                chips = groupedStudyLines.map {
                                    val studyLevel = StudyLevel.getById(it.levelId)

                                    Chip(
                                        id = studyLevel.id,
                                        label = stringResource(studyLevel.labelRes)
                                    )
                                }
                            )
                        }
                    )
                }
            }

            PrimaryButton(
                onClick = onNextClick,
                enabled = uiState.isNextEnabled,
                text = stringResource(Res.string.lbl_next),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Pds.spacing.Medium)
            )
        }
    }
}
