package com.ubb.fmi.orar.feature.groups.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.feature.groups.ui.viewmodel.model.GroupsUiState
import com.ubb.fmi.orar.ui.catalog.components.FailureState
import com.ubb.fmi.orar.ui.catalog.components.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.components.ProgressOverlay
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_group
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupsScreen(
    uiState: GroupsUiState,
    onGroupClick: (String) -> Unit,
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
    }
}
