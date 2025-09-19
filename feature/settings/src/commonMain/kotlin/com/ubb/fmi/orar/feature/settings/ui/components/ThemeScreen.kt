package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.domain.theme.model.ThemeOption
import com.ubb.fmi.orar.feature.settings.ui.model.labelRes
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemSelectable
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_theme
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen composable for theme setting selection
 */
@Composable
fun ThemeScreen(
    selectedThemeOption: ThemeOption,
    onThemeOptionClick: (ThemeOption) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_theme),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .padding(Pds.spacing.Medium)
        ) {
            ThemeOption.entries.forEach { themeOption ->
                ListItemSelectable(
                    headline = stringResource(themeOption.labelRes),
                    isSelected = themeOption == selectedThemeOption,
                    onClick = { onThemeOptionClick(themeOption) }
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewThemeScreen() {
    OrarUbbFmiTheme {
        ThemeScreen(
            selectedThemeOption = ThemeOption.SYSTEM,
            onThemeOptionClick = {},
            onBack = {}
        )
    }
}
