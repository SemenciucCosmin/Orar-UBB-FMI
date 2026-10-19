package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.feature.settings.ui.model.AboutLink
import com.ubb.fmi.orar.feature.settings.viewmodel.model.AboutUiState
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_about
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_developed_by
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_project
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_support
import org.jetbrains.compose.resources.stringResource

/**
 * Screen presenting details about the app, with links for support, feedback and the project.
 */
@Composable
fun AboutScreen(
    uiState: AboutUiState,
    appVersion: String?,
    onBack: () -> Unit,
    onLinkClick: (AboutLink) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_about),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Pds.spacing.Medium)
        ) {
            AboutHeader(
                appVersion = appVersion,
                modifier = Modifier.fillMaxWidth()
            )

            AboutSection(
                title = stringResource(Res.string.lbl_support),
                links = uiState.supportLinks,
                onLinkClick = onLinkClick
            )

            AboutSection(
                title = stringResource(Res.string.lbl_project),
                links = uiState.projectLinks,
                onLinkClick = onLinkClick
            )

            Text(
                text = stringResource(Res.string.lbl_developed_by),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAboutScreen() {
    OrarUbbFmiTheme {
        AboutScreen(
            uiState = AboutUiState(),
            appVersion = "1.3.0",
            onBack = {},
            onLinkClick = {},
        )
    }
}
