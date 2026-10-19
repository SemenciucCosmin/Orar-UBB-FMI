package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ubb.fmi.orar.feature.settings.ui.model.AboutLink
import com.ubb.fmi.orar.ui.catalog.components.list.ListItemClickable
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.collections.immutable.ImmutableList
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Titled group of [links]
 */
@Composable
fun AboutSection(
    title: String,
    links: ImmutableList<AboutLink>,
    onLinkClick: (AboutLink) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
        modifier = modifier
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(horizontal = Pds.spacing.XSmall)
        )

        links.forEach { link ->
            ListItemClickable(
                headline = stringResource(link.titleRes),
                underLine = stringResource(link.messageRes),
                leadingIcon = painterResource(link.iconRes),
                onClick = { onLinkClick(link) }
            )
        }
    }
}
