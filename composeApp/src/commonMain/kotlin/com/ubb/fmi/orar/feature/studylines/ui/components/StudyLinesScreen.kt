package com.ubb.fmi.orar.feature.studylines.ui.components

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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.StudyLinesUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.theme.Pds

@Composable
fun StudyLinesScreen(
    uiState: StudyLinesUiState,
    onStudyLineClick: (String) -> Unit,
    onStudyLevelClick: (String) -> Unit,
    onSelectFilter: (DegreeFilter) -> Unit,
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
                        items(DegreeFilter.entries.sortedBy { it.orderIndex }) { degreeFilter ->
                            FilterChip(
                                shape = CircleShape,
                                selected = uiState.selectedFilter == degreeFilter,
                                onClick = { onSelectFilter(degreeFilter) },
                                label = { Text(text = degreeFilter.label) }
                            )
                        }
                    }

                    LazyColumn(
                        contentPadding = PaddingValues(Pds.spacing.Medium),
                        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.filteredGroupedStudyLines) { groupedStudyLines ->
                            val fieldId = groupedStudyLines.firstOrNull()?.fieldId ?: return@items
                            val label = groupedStudyLines.firstOrNull()?.name ?: return@items

                            StudyLineListItem(
                                title = label,
                                isSelected = uiState.selectedFieldId == fieldId,
                                onClick = { onStudyLineClick(fieldId) },
                                onStudyLevelClick = onStudyLevelClick,
                                modifier = Modifier.fillMaxWidth(),
                                studyLevels = groupedStudyLines.map { studyLine ->
                                    StudyLevel.getById(studyLine.levelId)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
