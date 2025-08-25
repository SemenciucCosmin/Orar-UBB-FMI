package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.data.core.model.StudyYear
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.feature.studyLines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@Composable
fun StudyLinesFormScreen(
    uiState: StudyLinesFormUiState,
    onStudyLineClick: (String) -> Unit,
    onStudyYearClick: (String) -> Unit,
    onSelectFilter: (DegreeFilter) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold { paddingValues ->
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
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
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        items(uiState.filteredGroupedStudyLines) { groupedStudyLines ->
                            val lineId = groupedStudyLines.firstOrNull()?.baseId ?: return@items
                            val label = groupedStudyLines.firstOrNull()?.name ?: return@items

                            FormListItem(
                                modifier = Modifier.fillMaxWidth(),
                                headlineLabel = label,
                                isSelected = uiState.selectedStudyLineBaseId == lineId,
                                onClick = { onStudyLineClick(lineId) },
                                onUnderlineItemClick = onStudyYearClick,
                                selectedUnderlineItemId = uiState.selectedStudyYearId,
                                underlineItems = groupedStudyLines.map { studyLine ->
                                    val year = StudyYear.getById(studyLine.studyYearId)

                                    FormInputItem(
                                        id = year.id,
                                        label = year.label
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
                            .padding(16.dp)
                    ) {
                        Text(text = "NEXT")
                    }
                }
            }
        }
    }
}
