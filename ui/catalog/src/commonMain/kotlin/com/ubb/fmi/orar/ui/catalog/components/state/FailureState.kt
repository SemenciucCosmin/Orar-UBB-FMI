package com.ubb.fmi.orar.ui.catalog.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.ui.catalog.model.ErrorStatus
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_retry
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a failure state with an error message and a retry button.
 *
 * @param errorStatus specific status enum for specific error message display
 * @param onRetry Callback invoked when the retry button is clicked.
 * @param modifier Modifier to be applied to the column.
 */
@Composable
fun ErrorState(
    errorStatus: ErrorStatus,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(Pds.spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Large, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(errorStatus.labelRes),
            textAlign = TextAlign.Center
        )

        Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.small,
        ) {
            Text(text = stringResource(Res.string.lbl_retry))
        }
    }
}

@Preview
@Composable
private fun PreviewFailureState() {
    OrarUbbFmiTheme {
        ErrorState(
            errorStatus = ErrorStatus.GENERIC,
            onRetry = {},
        )
    }
}