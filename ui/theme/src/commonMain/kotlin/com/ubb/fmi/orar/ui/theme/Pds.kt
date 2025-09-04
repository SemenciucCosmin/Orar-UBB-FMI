package com.ubb.fmi.orar.ui.theme

import com.ubb.fmi.orar.ui.theme.dimension.DefaultIcon
import com.ubb.fmi.orar.ui.theme.dimension.DefaultSpacing
import com.ubb.fmi.orar.ui.theme.dimension.DefaultStroke
import com.ubb.fmi.orar.ui.theme.dimension.Icon
import com.ubb.fmi.orar.ui.theme.dimension.Spacing
import com.ubb.fmi.orar.ui.theme.dimension.Stroke

/**
 * Provides access to the default dimensions used in the application.
 */
object Pds {
    val spacing: Spacing
        get() = DefaultSpacing()

    val icon: Icon
        get() = DefaultIcon()

    val stroke: Stroke
        get() = DefaultStroke()
}
