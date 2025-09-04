package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_left_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

private const val MAX_LINES = 1

/**
 * A composable that displays a top app bar with a title, optional subtitle, and back navigation icon.
 *
 * @param title The title text to be displayed in the top bar.
 * @param modifier Modifier to be applied to the top bar.
 * @param subtitle Optional subtitle text to be displayed below the title.
 * @param onBack Callback invoked when the back button is clicked.
 * @param trailingContent Composable content to be displayed in the trailing area of the top bar.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    title: String,
    modifier: Modifier = Modifier,
    subtitle: String? = null,
    onBack: (() -> Unit)? = null,
    trailingContent: @Composable () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        actions = { trailingContent() },
        navigationIcon = {
            onBack?.let {
                IconButton(onClick = onBack) {
                    Icon(
                        modifier = Modifier.size(Pds.icon.Medium),
                        painter = painterResource(Res.drawable.ic_left_arrow),
                        contentDescription = null,
                    )
                }
            }
        },
        title = {
            Column(verticalArrangement = Arrangement.spacedBy(Pds.spacing.XSmall)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    overflow = when {
                        subtitle == null -> TextOverflow.Clip
                        else -> TextOverflow.Ellipsis
                    },
                    maxLines = when {
                        subtitle == null -> Int.MAX_VALUE
                        else -> MAX_LINES
                    }
                )

                subtitle?.let {
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewTopBarOneLine() {
    OrarUbbFmiTheme {
        TopBar(
            title = "Title",
            onBack = {}
        )
    }
}

@Preview
@Composable
private fun PreviewTopBarTwoLine() {
    OrarUbbFmiTheme {
        TopBar(
            title = "Title",
            subtitle = "Subtitle",
            onBack = {}
        )
    }
}