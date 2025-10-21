package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_home
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun NavigationCard(
    title: String,
    text: String,
    painter: Painter,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        onClick = onClick,
        color = MaterialTheme.colorScheme.tertiaryContainer,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
            modifier = Modifier
                .padding(Pds.spacing.SMedium)
                .fillMaxWidth()
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(Pds.spacing.Small)) {
                Icon(
                    modifier = Modifier.size(Pds.icon.Small),
                    painter = painter,
                    contentDescription = null,
                )

                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                )
            }

            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun NavigationButtonPreview() {
    OrarUbbFmiTheme {
        NavigationCard(
            text = "Home",
            title = "Navigate to home screen",
            painter = painterResource(Res.drawable.ic_home),
            onClick = {},
        )
    }
}