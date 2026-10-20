package com.ubb.fmi.orar.ui.catalog

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

/**
 * Height of the floating bottom bar drawn above the screens, 0 when it isn't displayed
 */
val LocalBottomBarPadding = compositionLocalOf { 0.dp }

/**
 * Bottom padding scrollable content needs at its end to stay clear of both the system bars and
 * the floating bottom bar, while still scrolling behind them.
 */
@Composable
fun PaddingValues.calculateContentBottomPadding(): Dp {
    return max(calculateBottomPadding(), LocalBottomBarPadding.current)
}
