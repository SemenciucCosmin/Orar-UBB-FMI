package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.domain.timetable.model.StudyLevel
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyLinesFormUiState.Companion.filteredGroupedStudyLines
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model.DegreeFilter
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.FormInputItem
import com.ubb.fmi.orar.ui.catalog.components.FormListItem
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_left_arrow
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyLinesFormScreen(
    uiState: StudyLinesFormUiState,
    onStudyLineClick: (String) -> Unit,
    onStudyLevelClick: (String) -> Unit,
    onSelectFilter: (DegreeFilter) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            if (!uiState.isLoading) {
                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = onBack) {
                            Icon(
                                modifier = Modifier.size(32.dp),
                                painter = painterResource(Res.drawable.ic_left_arrow),
                                contentDescription = null,
                            )
                        }
                    },
                    title = {
                        Text(
                            text = uiState.title,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
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
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.filteredGroupedStudyLines) { groupedStudyLines ->
                            val fieldId = groupedStudyLines.firstOrNull()?.fieldId ?: return@items
                            val label = groupedStudyLines.firstOrNull()?.name ?: return@items

                            FormListItem(
                                modifier = Modifier.fillMaxWidth(),
                                headlineLabel = label,
                                isSelected = uiState.selectedFieldId == fieldId,
                                onClick = { onStudyLineClick(fieldId) },
                                onUnderlineItemClick = onStudyLevelClick,
                                selectedUnderlineItemId = uiState.selectedStudyLevelId,
                                underlineItems = groupedStudyLines.map { studyLine ->
                                    val studyLevel = StudyLevel.getById(studyLine.levelId)

                                    FormInputItem(
                                        id = studyLevel.id,
                                        label = studyLevel.label
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
