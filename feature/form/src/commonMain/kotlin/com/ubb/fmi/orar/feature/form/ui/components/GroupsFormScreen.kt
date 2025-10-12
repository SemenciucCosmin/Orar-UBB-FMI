package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.data.timetable.model.Degree
import com.ubb.fmi.orar.data.timetable.model.Owner
import com.ubb.fmi.orar.data.timetable.model.StudyLevel
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.GroupsFromUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemSelectable
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.toImmutableList
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_next
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Composable screen that displays the list of groups for a certain study line that can
 * be selectable for configuring personal timetable
 * @param uiState: screen ui state
 * @param onGroupClick: lambda for group selection
 * @param onNextClick: lambda for next button action
 * @param onRetryClick: lambda for retry button action
 * @param onBack: lambda for back button action
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsFormScreen(
    uiState: GroupsFromUiState,
    onGroupClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        errorStatus = uiState.errorStatus,
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
        Column(modifier = Modifier.padding(paddingValues)) {
            LazyColumn(
                contentPadding = PaddingValues(Pds.spacing.Medium),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.groups) { group ->
                    ListItemSelectable(
                        headline = group.name,
                        isSelected = uiState.selectedGroupId == group.id,
                        onClick = { onGroupClick(group.id) },
                    )
                }
            }

            PrimaryButton(
                onClick = onNextClick,
                enabled = uiState.isNextEnabled,
                text = stringResource(Res.string.lbl_next),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(Pds.spacing.Medium)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewGroupsFormScreen() {
    OrarUbbFmiTheme {
        GroupsFormScreen(
            onGroupClick = {},
            onNextClick = {},
            onRetryClick = {},
            onBack = {},
            uiState = GroupsFromUiState(
                groups = List(5) {
                    Owner.Group(
                        id = "$it",
                        name = "$it",
                        configurationId = "$it",
                        studyLine = StudyLine(
                            id = "$it",
                            name = "$it",
                            fieldId = "$it",
                            level = StudyLevel.entries.random(),
                            degree = Degree.entries.random(),
                            configurationId = "$it"
                        )
                    )
                }.toImmutableList(),
                selectedGroupId = "916",
                title = "Informatica Engleza",
                studyLevel = StudyLevel.LEVEL_1,
                isLoading = false,
                errorStatus = null
            )
        )
    }
}
