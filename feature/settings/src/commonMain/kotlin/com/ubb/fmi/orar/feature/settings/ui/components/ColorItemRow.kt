package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.domain.colorpack.model.ColorPack
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.colorpack.components.ColorDot
import com.ubb.fmi.orar.ui.colorpack.extensions.getColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_reset
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_reset_to_default
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Settings row for a single [ColorItem]: its name, a reset to default action when customized
 * and a dot with its current color.
 *
 * @param item The color item displayed by the row.
 * @param colorPack The current colors, used to resolve the color and customization of [item].
 * @param onClick Callback invoked when the row is clicked.
 * @param onResetClick Callback invoked when the reset to default action is clicked.
 * @param modifier Modifier to be applied to the row.
 */
@Composable
fun ColorItemRow(
    item: ColorItem,
    colorPack: ColorPack,
    onClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ListItemClickable(
        headline = stringResource(item.labelRes),
        onClick = onClick,
        modifier = modifier,
        trailingContent = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (colorPack.isCustomized(item)) {
                    IconButton(
                        onClick = onResetClick,
                        modifier = Modifier.size(Pds.icon.SMedium),
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_reset),
                            contentDescription = stringResource(Res.string.lbl_reset_to_default),
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(Pds.icon.SMedium),
                        )
                    }
                }

                ColorDot(color = colorPack.getColor(item))
            }
        }
    )
}

@Preview
@Composable
private fun PreviewColorItemRow() {
    OrarUbbFmiTheme {
        Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium)) {
            ColorItemRow(
                item = ColorItem.EVENT_LECTURE,
                colorPack = ColorPack.Default,
                onClick = {},
                onResetClick = {},
            )

            ColorItemRow(
                item = ColorItem.EVENT_LECTURE,
                colorPack = ColorPack.Default.with(
                    item = ColorItem.EVENT_LECTURE,
                    color = ColorItem.EVENT_STAFF.defaultColor,
                ),
                onClick = {},
                onResetClick = {},
            )
        }
    }
}
