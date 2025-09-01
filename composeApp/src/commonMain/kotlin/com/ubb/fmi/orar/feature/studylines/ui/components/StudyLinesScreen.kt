package com.ubb.fmi.orar.feature.studylines.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.ui.catalog.components.ChipSelectionRow
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.ListItemExpandable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.model.Chip
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_study_line
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun StudyLinesScreen(
    uiState: StudyLinesUiState,
    onStudyLineClick: (String) -> Unit,
    onStudyLevelClick: (String) -> Unit,
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
                                        val studyLevel = StudyLevel.getById(it.levelId)
                                        ListItemClickable(
                                            headline = stringResource(studyLevel.labelRes),
                                            onClick = { onStudyLevelClick(studyLevel.id) },
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
    }
}
