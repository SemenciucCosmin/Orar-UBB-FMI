package com.ubb.fmi.orar.feature.news.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ubb.fmi.orar.ui.catalog.extensions.labelRes
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.datetime.Month
import org.jetbrains.compose.resources.stringResource

/**
 * Header of the articles published in the [month] of the [year]
 */
@Composable
fun NewsMonthHeader(
    year: Int,
    month: Month,
    modifier: Modifier = Modifier,
) {
    Text(
        text = "${stringResource(month.labelRes)} $year",
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = modifier.padding(top = Pds.spacing.Small)
    )
}
