package com.ubb.fmi.orar.feature.subjects.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState.Companion.filteredSubjects
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.components.SearchBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    uiState: SubjectsUiState,
    onSubjectClick: (String) -> Unit,
    onChangeSearchQuery: (String) -> Unit,
    onRetryClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    SearchBar(
                        value = uiState.searchQuery,
                        onValueChange = onChangeSearchQuery,
                        placeholder = "Disciplina",
                        onClearClick = { onChangeSearchQuery(String.BLANK) },
                        modifier = Modifier.padding(end = 16.dp)
                    )
                }
            ).takeIf { !uiState.isError && !uiState.isLoading }
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

            uiState.filteredSubjects.isEmpty() -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    Text(
                        text = "No results found",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(uiState.filteredSubjects) { subject ->
                        SubjectListItem(
                            id = subject.id,
                            title = subject.name,
                            onClick = { onSubjectClick(subject.id) },
                        )
                    }
                }
            }
        }
    }
}
