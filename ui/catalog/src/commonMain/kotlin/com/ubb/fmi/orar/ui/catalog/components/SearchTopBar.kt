package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_left_arrow
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a top app bar with an optional title, back navigation
 * icon and a search bar if enabled
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchTopBar(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    title: String,
    isSearchEnabled: Boolean,
    onClearClick: () -> Unit,
    onBack: (() -> Unit),
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    modifier = Modifier.size(Pds.icon.Medium),
                    painter = painterResource(Res.drawable.ic_left_arrow),
                    contentDescription = null,
                )
            }
        },
        title = {
            when {
                isSearchEnabled -> {
                    SearchBar(
                        value = value,
                        onValueChange = onValueChange,
                        placeholder = placeholder,
                        onClearClick = onClearClick,
                    )
                }

                else -> {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewSearchTopBar() {
    OrarUbbFmiTheme {
        SearchTopBar(
            title = "Title",
            value = "Query",
            placeholder = "Search something",
            isSearchEnabled = true,
            onValueChange = {},
            onClearClick = {},
            onBack = {},
        )
    }
}
