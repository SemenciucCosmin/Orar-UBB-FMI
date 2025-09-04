package com.ubb.fmi.orar.ui.catalog.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A utility function to conditionally apply a modifier based on a boolean condition.
 *
 * @param condition The condition to check.
 * @param modifier The modifier to apply if the condition is true.
 * @return The original modifier if the condition is false, or the modified one if true.
 */
@Composable
fun Modifier.conditional(
    condition: Boolean,
    modifier: @Composable Modifier.() -> Modifier
): Modifier {
    return when {
        condition -> then(modifier(Modifier))
        else -> this
    }
}