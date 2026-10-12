@file:Suppress("MatchingDeclarationName")

package com.ubb.fmi.orar.ui.catalog.components.timetable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.ui.colorpack.LocalColorPack
import com.ubb.fmi.orar.ui.colorpack.extensions.getColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_now
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_up_next
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/**
 * Visual configurations for [StatusIndicator].
 */
sealed class StatusIndicator {

    /**
     * The customizable color item providing the color of both the dot and the label.
     */
    abstract val colorItem: ColorItem

    /**
     * The label displayed next to the dot.
     */
    abstract val label: StringResource

    /**
     * Marks something happening right now, such as an ongoing event, the current day or week.
     */
    data class Ongoing(
        override val colorItem: ColorItem = ColorItem.STATUS_ONGOING,
        override val label: StringResource = Res.string.lbl_now,
    ) : StatusIndicator()

    /**
     * Marks something about to happen, such as the next event.
     */
    data class Upcoming(
        override val colorItem: ColorItem = ColorItem.STATUS_UPCOMING,
        override val label: StringResource = Res.string.lbl_up_next,
    ) : StatusIndicator()

    companion object {

        /**
         * The default spacing between the dot and the label.
         */
        val SPACING: Dp
            @Composable get() = Pds.spacing.XSmall

        /**
         * The default diameter of the dot.
         */
        val DOT_SIZE: Dp = Pds.spacing.XSmall

        /**
         * The default text style of the label.
         */
        val TEXT_STYLE: TextStyle
            @Composable get() = MaterialTheme.typography.labelSmall
    }
}

/**
 * A colored dot followed by a short status label, such as "• now".
 *
 * @param defaults The visual configuration providing the color and the label.
 * @param modifier Modifier to be applied to the row.
 * @param textStyle The text style of the label.
 * @param spacing The spacing between the dot and the label.
 * @param dotSize The diameter of the dot.
 */
@Composable
fun StatusIndicator(
    defaults: StatusIndicator,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = StatusIndicator.TEXT_STYLE,
    spacing: Dp = StatusIndicator.SPACING,
    dotSize: Dp = StatusIndicator.DOT_SIZE,
) {
    val color = LocalColorPack.current.getColor(defaults.colorItem)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(dotSize)
                .background(color = color, shape = CircleShape)
        )

        Text(
            style = textStyle,
            text = stringResource(defaults.label),
            color = color
        )
    }
}

@Preview
@Composable
private fun PreviewStatusIndicator() {
    OrarUbbFmiTheme {
        Column {
            StatusIndicator(defaults = StatusIndicator.Ongoing())
            StatusIndicator(defaults = StatusIndicator.Upcoming())
        }
    }
}
