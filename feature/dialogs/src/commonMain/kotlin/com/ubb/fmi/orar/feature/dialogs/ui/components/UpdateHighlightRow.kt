package com.ubb.fmi.orar.feature.dialogs.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.ubb.fmi.orar.feature.dialogs.ui.model.UpdateHighlight
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * Row of [AppUpdateAnnouncement] presenting an update [highlight]
 */
@Composable
fun UpdateHighlightRow(
    highlight: UpdateHighlight,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .size(Pds.icon.Large)
                .background(color = MaterialTheme.colorScheme.primaryContainer, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(highlight.iconRes),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(Pds.icon.Small)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XXSmall)) {
            Text(
                text = stringResource(highlight.titleRes),
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.SemiBold,
            )

            Text(
                text = stringResource(highlight.messageRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
