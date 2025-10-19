package com.ubb.fmi.orar.ui.catalog.components.custom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DialogProperties
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertDialog(
    text: String,
    positiveButtonText: String,
    negativeButtonText: String,
    onPositiveClick: () -> Unit,
    onNegativeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    BasicAlertDialog(
        modifier = modifier,
        onDismissRequest = onNegativeClick,
        properties = DialogProperties(),
        content = {
            Surface(shape = MaterialTheme.shapes.medium) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(Pds.spacing.Medium)
                        .width(IntrinsicSize.Max)
                ) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(
                            Pds.spacing.Medium,
                            Alignment.End
                        )
                    ) {
                        TextButton(onClick = onNegativeClick) {
                            Text(text = negativeButtonText)
                        }

                        TextButton(onClick = onPositiveClick) {
                            Text(text = positiveButtonText)
                        }
                    }
                }
            }
        }
    )
}

@Preview
@Composable
private fun PreviewAlertDialog() {
    OrarUbbFmiTheme {
        AlertDialog(
            text = "Is this the preview of an AlertDialog?",
            positiveButtonText = "Yes",
            negativeButtonText = "Also yes",
            onPositiveClick = {},
            onNegativeClick = {}
        )
    }
}
