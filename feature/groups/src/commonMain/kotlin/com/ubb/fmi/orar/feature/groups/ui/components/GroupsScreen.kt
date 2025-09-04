package com.ubb.fmi.orar.feature.groups.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.groups.ui.viewmodel.model.GroupsUiState
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.model.StudyLevel
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.persistentListOf
import orar_ubb_fmi.feature.groups.generated.resources.Res
import orar_ubb_fmi.feature.groups.generated.resources.ic_group
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable function that displays a screen with a list of groups.
 * It shows a loading state while fetching data, an error state if fetching fails,
 * and a list of groups when data is successfully fetched.
 * The screen includes a top bar with a title and subtitle,
 * and allows navigation back to the previous screen.
 * @param uiState The current state of the groups UI, containing loading, error, and group data.
 * @param onGroupClick Callback function to handle group item clicks, passing the group ID.
 * @param onRetryClick Callback function to handle retry actions when an error occurs.
 * @param onBack Callback function to handle back navigation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    uiState: GroupsUiState,
    onGroupClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        isError = uiState.isError,
        onRetryClick = onRetryClick,
        topBar = {
            if (uiState.title != null && uiState.studyLevel != null) {
                TopBar(
                    title = uiState.title,
                    subtitle = stringResource(uiState.studyLevel.labelRes),
                    onBack = onBack
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            contentPadding = PaddingValues(Pds.spacing.Medium),
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier.padding(paddingValues)
        ) {
            items(uiState.groups) { group ->
                ListItemClickable(
                    headline = group,
                    onClick = { onGroupClick(group) },
                    leadingIcon = painterResource(Res.drawable.ic_group),
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewGroupsScreen() {
    OrarUbbFmiTheme {
        GroupsScreen(
            onGroupClick = {},
            onRetryClick = {},
            onBack = {},
            uiState = GroupsUiState(
                groups = persistentListOf("913", "914", "915", "916", "917"),
                title = "Informatica Engleza",
                studyLevel = StudyLevel.LEVEL_1,
                isLoading = false,
                isError = false
            )
        )
    }
}
