package com.ubb.fmi.orar.ui.colorpack.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

/**
 * A filled circle displaying [color], outlined so it stays visible on any background.
 *
 * @param color The color of the dot.
 * @param modifier Modifier to be applied to the dot.
 * @param size The diameter of the dot.
 */
@Composable
fun ColorDot(
    color: Color,
    modifier: Modifier = Modifier,
    size: Dp = Pds.icon.SMedium,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color = color, shape = CircleShape)
            .border(
                width = Pds.stroke.Medium,
                color = MaterialTheme.colorScheme.outlineVariant,
                shape = CircleShape
            )
    )
}

@Preview
@Composable
private fun PreviewColorDot() {
    OrarUbbFmiTheme {
        ColorDot(color = Color.Red)
    }
}
