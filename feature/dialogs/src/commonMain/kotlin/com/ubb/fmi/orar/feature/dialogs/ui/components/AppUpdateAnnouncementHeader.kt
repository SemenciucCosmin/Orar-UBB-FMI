package com.ubb.fmi.orar.feature.dialogs.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_subtitle
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_app_update_announcement_title
import org.jetbrains.compose.resources.stringResource

/**
 * Title and subtitle of [AppUpdateAnnouncement]
 */
@Composable
fun AppUpdateAnnouncementHeader(modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Text(
            text = stringResource(Res.string.lbl_app_update_announcement_title),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
        )

        Text(
            text = stringResource(Res.string.lbl_app_update_announcement_subtitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
