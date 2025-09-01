package com.ubb.fmi.orar.feature.subjects.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState.Companion.filteredSubjects
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.components.SearchBar
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_subject
import orar_ubb_fmi.composeapp.generated.resources.lbl_no_results
import orar_ubb_fmi.composeapp.generated.resources.lbl_subject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    uiState: SubjectsUiState,
    onSubjectClick: (String) -> Unit,
    onChangeSearchQuery: (String) -> Unit,
    onRetryClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = bottomBar,
        topBar = {
            if (!uiState.isError && !uiState.isLoading) {
                TopAppBar(
                    title = {
                        SearchBar(
                            value = uiState.searchQuery,
                            onValueChange = onChangeSearchQuery,
                            placeholder = stringResource(Res.string.lbl_subject),
                            onClearClick = { onChangeSearchQuery(String.BLANK) },
                            modifier = Modifier.padding(end = Pds.spacing.Medium)
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

            uiState.filteredSubjects.isEmpty() -> {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(Pds.spacing.Medium)
                        .fillMaxSize()
                ) {
                    Text(
                        text = stringResource(Res.string.lbl_no_results),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            else -> {
                LazyColumn(
                    contentPadding = PaddingValues(Pds.spacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                    modifier = Modifier.padding(paddingValues)
                ) {
                    items(uiState.filteredSubjects) { subject ->
                        ListItemClickable(
                            headline = subject.name,
                            overline = subject.id,
                            onClick = { onSubjectClick(subject.id) },
                            leadingIcon = painterResource(Res.drawable.ic_subject),
                        )
                    }
                }
            }
        }
    }
}
