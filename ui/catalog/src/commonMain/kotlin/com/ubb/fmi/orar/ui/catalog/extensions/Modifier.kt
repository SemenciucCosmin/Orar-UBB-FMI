package com.ubb.fmi.orar.ui.catalog.extensions

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

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