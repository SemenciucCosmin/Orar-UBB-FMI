package com.ubb.fmi.orar.feature.subjects.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.TimetableOwner
import com.ubb.fmi.orar.domain.extensions.BLANK
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState
import com.ubb.fmi.orar.feature.subjects.ui.viewmodel.model.SubjectsUiState.Companion.filteredSubjects
import com.ubb.fmi.orar.ui.catalog.components.SearchBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.feature.subjects.generated.resources.Res
import orar_ubb_fmi.feature.subjects.generated.resources.ic_subject
import orar_ubb_fmi.feature.subjects.generated.resources.lbl_subject
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays the Subjects screen.
 * It shows a list of subjects with a search bar at the top.
 * It handles loading, empty, and error states using [StateScaffold].
 * @param uiState The current state of the subjects UI.
 * @param onSubjectClick Callback invoked when a subject is clicked, passing the subject ID.
 * @param onChangeSearchQuery Callback invoked when the search query changes.
 * @param onRetryClick Callback invoked when the user wants to retry loading subjects.
 * @param bottomBar Composable function for the bottom bar, if any.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectsScreen(
    uiState: SubjectsUiState,
    onSubjectClick: (String) -> Unit,
    onChangeSearchQuery: (String) -> Unit,
    onRetryClick: () -> Unit,
    bottomBar: @Composable () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        isEmpty = uiState.filteredSubjects.isEmpty(),
        isError = uiState.isError,
        onRetryClick = onRetryClick,
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

@Preview
@Composable
private fun PreviewSubjectsScreen() {
    OrarUbbFmiTheme {
        SubjectsScreen(
            onSubjectClick = {},
            onChangeSearchQuery = {},
            onRetryClick = {},
            bottomBar = {},
            uiState = SubjectsUiState(
                searchQuery = "",
                isLoading = false,
                isError = false,
                subjects = List(5) {
                    TimetableOwner.Subject(
                        id = it.toString(),
                        name = "Subject $it",
                        configurationId = "20241",
                    )
                }.toImmutableList()
            )
        )
    }
}
