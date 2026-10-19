package com.ubb.fmi.orar.ui.catalog.components.calendar

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.domain.colorpack.model.ColorItem
import com.ubb.fmi.orar.ui.colorpack.LocalColorPack
import com.ubb.fmi.orar.ui.colorpack.extensions.getColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

private const val ADJACENT_MONTH_ALPHA = 0.4f
private const val TINT_ALPHA = 0.12f
private const val INDICATOR_WIDTH_FRACTION = 0.5f

/**
 * An outlined square box displaying a day of the month, subtly tinted with the color of
 * [colorItem] from the [LocalColorPack] and marked by a small indicator of that color.
 * Today is outlined with the primary color and days of an adjacent month are faded.
 *
 * @param dayOfMonth The day of the month to display.
 * @param colorItem The color item of the academic activity the day is in.
 * @param isToday Whether the day is today.
 * @param isCurrentMonth Whether the day belongs to the displayed month.
 * @param modifier Modifier to be applied to the box.
 */
@Composable
fun CalendarDayBox(
    dayOfMonth: Int,
    colorItem: ColorItem,
    isToday: Boolean,
    isCurrentMonth: Boolean,
    modifier: Modifier = Modifier,
) {
    val color = LocalColorPack.current.getColor(colorItem)

    Surface(
        shape = MaterialTheme.shapes.small,
        color = color.copy(alpha = TINT_ALPHA),
        contentColor = MaterialTheme.colorScheme.onSurface,
        border = when {
            isToday -> BorderStroke(Pds.stroke.Medium * 2, MaterialTheme.colorScheme.primary)
            else -> BorderStroke(Pds.stroke.Medium, MaterialTheme.colorScheme.outlineVariant)
        },
        modifier = modifier
            .aspectRatio(1f)
            .alpha(
                when {
                    isCurrentMonth -> 1f
                    else -> ADJACENT_MONTH_ALPHA
                }
            )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = when {
                    isToday -> FontWeight.Bold
                    else -> FontWeight.Normal
                },
                modifier = Modifier.align(Alignment.Center)
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = Pds.spacing.XSmall)
                    .fillMaxWidth(INDICATOR_WIDTH_FRACTION)
                    .height(Pds.spacing.XXSmall)
                    .background(color = color, shape = CircleShape)
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCalendarDayBox() {
    OrarUbbFmiTheme {
        CalendarDayBox(
            dayOfMonth = 15,
            colorItem = ColorItem.CALENDAR_TEACHING,
            isToday = true,
            isCurrentMonth = true,
        )
    }
}
