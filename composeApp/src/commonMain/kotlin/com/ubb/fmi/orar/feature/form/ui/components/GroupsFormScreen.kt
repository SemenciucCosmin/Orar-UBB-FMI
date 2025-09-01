package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.GroupsFromUiState
import com.ubb.fmi.orar.feature.form.ui.viewmodel.model.isNextEnabled
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemSelectable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_next
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsFormScreen(
    uiState: GroupsFromUiState,
    onGroupClick: (String) -> Unit,
    onNextClick: () -> Unit,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
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
                        contentPadding = PaddingValues(Pds.spacing.Medium),
                        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                        modifier = Modifier.weight(1f)
                    ) {
                        items(uiState.groups) { group ->
                            ListItemSelectable(
                                headline = group,
                                isSelected = uiState.selectedGroupId == group,
                                onClick = { onGroupClick(group) },
                            )
                        }
                    }

                    Button(
                        onClick = onNextClick,
                        enabled = uiState.isNextEnabled,
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Pds.spacing.Medium)
                    ) {
                        Text(text = stringResource(Res.string.lbl_next))
                    }
                }
            }
        }
    }
}
