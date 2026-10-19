package com.ubb.fmi.orar.feature.settings.ui.route

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ubb.fmi.orar.domain.extensions.getAppVersion
import com.ubb.fmi.orar.domain.extensions.openUrl
import com.ubb.fmi.orar.feature.settings.ui.components.AboutScreen
import com.ubb.fmi.orar.feature.settings.viewmodel.AboutViewModel
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_generic_error_message
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

/**
 * Route for the about screen
 */
@Composable
fun AboutRoute(navController: NavController) {
    val context = getContext()
    val appVersion = getAppVersion(context)
    val errorMessage = stringResource(Res.string.lbl_generic_error_message)
    val viewModel: AboutViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AboutScreen(
        uiState = uiState,
        appVersion = appVersion,
        onBack = navController::navigateUp,
        onLinkClick = { link ->
            viewModel.onLinkClick(link)
            openUrl(link.url, context) {
                showToast(context, errorMessage, ToastLength.SHORT)
            }
        }
    )
}
