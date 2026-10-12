package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.domain.colorpack.model.ColorGroup
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_colors
import org.jetbrains.compose.resources.stringResource

/**
 * Screen listing every customizable color, grouped by [ColorGroup].
 * Each row shows the current color and, when customized, a reset to default action.
 */
@Composable
fun ColorsScreen(
    colorPack: ColorPack,
    onItemClick: (ColorItem) -> Unit,
    onResetClick: (ColorItem) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_colors),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Large),
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(Pds.spacing.Medium)
        ) {
            ColorGroup.entries.forEach { group ->
                Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium)) {
                    Text(
                        text = stringResource(group.labelRes),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    ColorItem.entries.filter { it.group == group }.forEach { item ->
                        ColorItemRow(
                            item = item,
                            colorPack = colorPack,
                            onClick = { onItemClick(item) },
                            onResetClick = { onResetClick(item) },
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewColorsScreen() {
    OrarUbbFmiTheme {
        ColorsScreen(
            colorPack = ColorPack(
                customColors = mapOf(ColorItem.EVENT_LECTURE to ColorItem.EVENT_STAFF.defaultColor)
            ),
            onItemClick = {},
            onResetClick = {},
            onBack = {},
        )
    }
}
