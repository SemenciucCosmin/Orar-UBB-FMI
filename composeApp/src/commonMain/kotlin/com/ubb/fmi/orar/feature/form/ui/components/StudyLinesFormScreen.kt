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
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.ui.catalog.components.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemExpandable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_next
import org.jetbrains.compose.resources.stringResource

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

                    Button(
                        onClick = onNextClick,
                        enabled = uiState.isNextEnabled,
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
