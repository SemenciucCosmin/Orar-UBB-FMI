@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.ui.colorpack.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

/**
 * Preset colors offered as quick picks, chosen to read well on both light and dark themes.
 */
val DefaultSwatches: List<Color> = listOf(
    Color(0xFFDC2626),
    Color(0xFFEA580C),
    Color(0xFFD97706),
    Color(0xFFFFAC1C),
    Color(0xFF65A30D),
    Color(0xFF16A34A),
    Color(0xFF1DB954),
    Color(0xFF0D9488),
    Color(0xFF0891B2),
    Color(0xFF2563EB),
    Color(0xFF4F46E5),
    Color(0xFF7C3AED),
    Color(0xFFC026D3),
    Color(0xFFDB2777),
    Color(0xFF78716C),
    Color(0xFF475569),
)

/**
 * A wrapping grid of preset color dots.
 *
 * @param swatches The colors to display.
 * @param selectedColor The currently selected color, highlighted when present in [swatches].
 * @param onSwatchClick Callback with the clicked color.
 * @param modifier Modifier to be applied to the grid.
 */
@Composable
fun ColorSwatches(
    selectedColor: Color,
    onSwatchClick: (Color) -> Unit,
    modifier: Modifier = Modifier,
    swatches: List<Color> = DefaultSwatches,
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
    ) {
        swatches.forEach { swatch ->
            val isSelected = swatch == selectedColor
            ColorDot(
                color = swatch,
                size = Pds.icon.Medium,
                modifier = Modifier
                    .border(
                        width = Pds.spacing.XXSmall,
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.primary
                            else -> Color.Transparent
                        },
                        shape = CircleShape,
                    )
                    .padding(Pds.spacing.XSmall)
                    .clip(CircleShape)
                    .clickable { onSwatchClick(swatch) }
            )
        }
    }
}

@Preview
@Composable
private fun PreviewColorSwatches() {
    OrarUbbFmiTheme {
        ColorSwatches(selectedColor = DefaultSwatches.first(), onSwatchClick = {})
    }
}
