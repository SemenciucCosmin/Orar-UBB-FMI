package com.ubb.fmi.orar.feature.news.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.extensions.openUrl
import com.ubb.fmi.orar.feature.news.ui.components.NewsScreen
import com.ubb.fmi.orar.feature.news.ui.viewmodel.NewsViewModel
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import com.ubb.fmi.orar.ui.navigation.components.BottomBar
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_generic_error_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Composable function that represents the News route in the application.
 */
@Composable
fun NewsRoute(navController: NavController) {
    val context = getContext()
    val viewModel: NewsViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val toastMessage = stringResource(Res.string.lbl_generic_error_message)

    NewsScreen(
        uiState = uiState,
        onRetryClick = viewModel::retry,
        bottomBar = { BottomBar(navController) },
        onArticleClick = { url ->
            openUrl(url, context) { showToast(context, toastMessage, ToastLength.SHORT) }
        }
    )
}
