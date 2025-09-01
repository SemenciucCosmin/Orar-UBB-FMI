package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.ubb.fmi.orar.ui.theme.Pds

@Composable
fun ListRow(
    headline: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    headlineTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    overline: String? = null,
    underLine: String? = null,
    leadingIcon: Painter? = null,
    trailingIcon: Painter? = null,
    trailingIconSize: Dp = Pds.icon.Medium,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
        verticalAlignment = Alignment.CenterVertically
    ) {
        leadingIcon?.let {
            Icon(
                modifier = Modifier.size(Pds.icon.SMedium),
                painter = leadingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.XXSmall),
        ) {
            overline?.let {
                Text(
                    text = overline,
                    textAlign = textAlign,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Text(
                text = headline,
                textAlign = textAlign,
                style = headlineTextStyle,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.fillMaxWidth()
            )

            underLine?.let {
                Text(
                    text = underLine,
                    textAlign = textAlign,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        trailingIcon?.let {
            Icon(
                modifier = Modifier.size(trailingIconSize),
                painter = trailingIcon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
