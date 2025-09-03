package com.ubb.fmi.orar.ui.catalog.components.state

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_error_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_retry
import org.jetbrains.compose.resources.stringResource

@Composable
fun FailureState(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Large, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(Res.string.lbl_error_message))

        Button(
            onClick = onRetry,
            shape = MaterialTheme.shapes.small,
        ) {
            Text(text = stringResource(Res.string.lbl_retry))
        }
    }
}