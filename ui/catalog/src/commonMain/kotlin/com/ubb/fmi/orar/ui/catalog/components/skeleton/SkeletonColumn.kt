package com.ubb.fmi.orar.ui.catalog.components.skeleton

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.unit.Dp
import com.ubb.fmi.orar.ui.catalog.extensions.rememberShimmerBrush
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_loading
import org.jetbrains.compose.resources.stringResource

/**
 * Non scrollable container for skeleton placeholders, filling the available space and clipping
 * whatever doesn't fit. Accessibility services announce it as a single loading element.
 *
 * @param contentPadding Padding around the placeholders.
 * @param spacing Vertical spacing between the placeholders.
 * @param content Placeholders, receiving the shimmering brush they should be painted with.
 */
@Composable
fun SkeletonColumn(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(Pds.spacing.Medium),
    spacing: Dp = Pds.spacing.Medium,
    content: @Composable ColumnScope.(Brush) -> Unit,
) {
    val brush = rememberShimmerBrush()
    val loadingDescription = stringResource(Res.string.lbl_loading)

    Column(
        verticalArrangement = Arrangement.spacedBy(spacing),
        modifier = modifier
            .fillMaxSize()
            .clipToBounds()
            .clearAndSetSemantics { contentDescription = loadingDescription }
            .padding(contentPadding),
        content = { content(brush) }
    )
}
