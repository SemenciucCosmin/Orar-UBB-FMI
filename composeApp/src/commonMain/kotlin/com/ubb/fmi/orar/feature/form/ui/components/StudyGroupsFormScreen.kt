package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.StudyGroupsFromUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.FormListItem
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay

@Composable
fun StudyGroupsFormScreen(
    uiState: StudyGroupsFromUiState,
    onStudyGroupClick: (StudyGroupsFromUiState.Group) -> Unit,
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
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.studyGroups) { studyGroup ->
                            FormListItem<String>(
                                modifier = Modifier.fillMaxWidth(),
                                headlineLabel = studyGroup.id,
                                underLineLabel = studyGroup.type.label,
                                isSelected = uiState.selectedStudyGroup == studyGroup,
                                onClick = { onStudyGroupClick(studyGroup) },
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
