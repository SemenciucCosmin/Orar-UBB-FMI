package com.ubb.fmi.orar.ui.catalog.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_no_results
import org.jetbrains.compose.resources.stringResource

/**
 * A composable that provides a scaffold layout with different states: loading, empty, error, or content.
 * It displays a top bar, bottom bar, snackbar host, and floating action button,
 * and handles different states of the content.
 * @param modifier Modifier to be applied to the scaffold.
 * @param isLoading Indicates if the content is currently loading.
 * @param isEmpty Indicates if the content is empty.
 * @param isError Indicates if there was an error loading the content.
 * @param onRetryClick Callback invoked when the retry button is clicked in case of an error.
 * @param topBar Composable for the top bar of the scaffold.
 * @param bottomBar Composable for the bottom bar of the scaffold.
 * @param snackbarHost Composable for the snackbar host of the scaffold.
 * @param floatingActionButton Composable for the floating action button of the scaffold.
 * @param floatingActionButtonPosition Position of the floating action button.
 * @param containerColor Color of the scaffold container.
 * @param contentColor Color of the content within the scaffold.
 * @param contentWindowInsets Insets for the content area of the scaffold.
 * @param content Composable that provides the main content of the scaffold when not in
 * loading, empty, or error state.
 */
@Composable
fun StateScaffold(
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    isEmpty: Boolean = false,
    isError: Boolean = false,
    onRetryClick: () -> Unit = {},
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        bottomBar = bottomBar,
        snackbarHost = snackbarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor,
        contentWindowInsets = contentWindowInsets,
    ) { paddingValues ->
        when {
            isLoading -> ProgressOverlay(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )

            isError -> FailureState(
                onRetry = onRetryClick,
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            )

            isEmpty -> Column(
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

            else -> content(paddingValues)
        }
    }
}
