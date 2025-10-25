@file:Suppress("MagicNumber")

package com.ubb.fmi.orar.feature.feedback.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.ubb.fmi.orar.feature.feedback.ui.model.FeedbackChoice
import com.ubb.fmi.orar.ui.catalog.components.PrimaryButton
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_ask_me_later
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen with feedback message and action buttons for google in app
 * review or support web page redirect
 */
@Composable
fun FeedbackOutcomeScreen(
    feedbackChoice: FeedbackChoice,
    onAskMeLaterClick: () -> Unit,
    onActionClick: () -> Unit,
    onSecondaryActionClick: () -> Unit,
    onDismissClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(contentWindowInsets = WindowInsets.safeDrawing) { paddingValues ->
        Surface(modifier = modifier.padding(paddingValues)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onAskMeLaterClick) {
                        Text(
                            text = stringResource(Res.string.lbl_feedback_ask_me_later),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(0.4f))

                Column(
                    modifier = Modifier.padding(horizontal = Pds.spacing.Medium),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    FilledIconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(feedbackChoice.iconRes),
                            contentDescription = null,
                            modifier = Modifier.size(Pds.icon.XLarge)
                        )
                    }

                    Spacer(modifier = Modifier.height(Pds.spacing.XLarge))

                    Text(
                        text = stringResource(feedbackChoice.titleRes),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(Pds.spacing.Small))

                    Text(
                        text = stringResource(feedbackChoice.subtitleRes),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.weight(0.6f))

                PrimaryButton(
                    text = stringResource(feedbackChoice.actionLabelRes),
                    onClick = onActionClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Pds.spacing.Medium)
                )

                feedbackChoice.secondaryActionLabelRes?.let {
                    PrimaryButton(
                        text = stringResource(it),
                        onClick = onSecondaryActionClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = Pds.spacing.Medium)
                    )
                }

                TextButton(onClick = onDismissClick) {
                    Text(
                        text = stringResource(feedbackChoice.dismissLabelRes),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                Spacer(modifier = Modifier.height(Pds.spacing.Medium))
            }
        }
    }
}

@Preview
@Composable
private fun PreviewFeedbackOutcomeScreen() {
    OrarUbbFmiTheme {
        FeedbackOutcomeScreen(
            feedbackChoice = FeedbackChoice.GREAT,
            onAskMeLaterClick = {},
            onActionClick = {},
            onSecondaryActionClick = {},
            onDismissClick = {}
        )
    }
}
