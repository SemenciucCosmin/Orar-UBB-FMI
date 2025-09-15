package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.ubb.fmi.orar.domain.extensions.DOT
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.feature.settings.generated.resources.Res
import orar_ubb_fmi.feature.settings.generated.resources.lbl_change_configuration
import orar_ubb_fmi.feature.settings.generated.resources.lbl_github
import orar_ubb_fmi.feature.settings.generated.resources.lbl_open_source
import orar_ubb_fmi.feature.settings.generated.resources.lbl_settings
import orar_ubb_fmi.feature.settings.generated.resources.lbl_theme
import orar_ubb_fmi.feature.settings.generated.resources.lbl_version
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen for all settings options.
 * Theme and configuration change
 */
@Composable
fun SettingsScreen(
    appVersion: String?,
    developerName: String,
    onGithubUrlClick: () -> Unit,
    onBack: () -> Unit,
    onChangeConfigurationClick: () -> Unit,
    onThemeClick: () -> Unit,
) {
    val annotatedGithubUrl = buildAnnotatedString {
        withStyle(SpanStyle(MaterialTheme.colorScheme.onBackground)) {
            append(stringResource(Res.string.lbl_open_source))
            append(String.SPACE)
        }

        pushLink(
            LinkAnnotation.Clickable(
                tag = stringResource(Res.string.lbl_github),
                linkInteractionListener = { onGithubUrlClick() },
                styles = TextLinkStyles(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline
                    )
                )
            )
        )

        append(stringResource(Res.string.lbl_github))

        pop()
        append(String.DOT)
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_settings),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(Pds.spacing.Medium)
        ) {
            ListItemClickable(
                headline = stringResource(Res.string.lbl_change_configuration),
                onClick = onChangeConfigurationClick
            )

            ListItemClickable(
                headline = stringResource(Res.string.lbl_theme),
                onClick = onThemeClick
            )

            Spacer(modifier = Modifier.weight(1f))

            Column(
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                appVersion?.let{
                    Text(
                        text = stringResource(Res.string.lbl_version, appVersion),
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Text(
                    text = developerName,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )

                Text(
                    text = annotatedGithubUrl,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewSettingsScreen() {
    OrarUbbFmiTheme {
        SettingsScreen(
            appVersion = "1.0.0",
            developerName = "Semenciuc Cosmin",
            onGithubUrlClick = {},
            onBack = {},
            onChangeConfigurationClick = {},
            onThemeClick = {}
        )
    }
}
