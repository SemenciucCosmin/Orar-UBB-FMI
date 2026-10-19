package com.ubb.fmi.orar.ui.catalog.components.skeleton

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

private val BAR_SHAPE = RoundedCornerShape(4.dp)

/**
 * A placeholder bar painted with the shimmering [brush]. Its width is given through [modifier].
 */
@Composable
fun ShimmerBar(
    brush: Brush,
    height: Dp,
    modifier: Modifier = Modifier,
    shape: Shape = BAR_SHAPE,
) {
    Box(
        modifier = modifier
            .height(height)
            .background(brush = brush, shape = shape)
    )
}
