package com.ubb.fmi.orar.feature.feedback.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_ask_me_later
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_choice_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_feedback_dont_ask_again
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Screen with feedback response choices and dismiss and postpone actions.
 */
@Suppress("MagicNumber")
@Composable
fun FeedbackChoiceScreen(
    onFeedbackChoiceClick: (FeedbackChoice) -> Unit,
    onAskMeLaterClick: () -> Unit,
    onDontAskAgainClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold { paddingValues ->
        Surface(modifier = modifier.padding(paddingValues)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.weight(0.5f))

                Column(
                    modifier = Modifier.padding(horizontal = Pds.spacing.Medium),
                    verticalArrangement = Arrangement.spacedBy(Pds.spacing.XXLarge),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(Res.string.lbl_feedback_choice_message),
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        FeedbackChoice.entries.forEach { feedbackChoiceType ->
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(Pds.spacing.Small),
                            ) {
                                FilledIconButton(
                                    onClick = { onFeedbackChoiceClick(feedbackChoiceType) }
                                ) {
                                    Icon(
                                        painter = painterResource(feedbackChoiceType.iconRes),
                                        contentDescription = null,
                                        modifier = Modifier.size(Pds.icon.XLarge)
                                    )
                                }

                                Text(
                                    text = stringResource(feedbackChoiceType.labelRes),
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.5f))

                TextButton(onClick = onAskMeLaterClick) {
                    Text(
                        text = stringResource(Res.string.lbl_feedback_ask_me_later),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }

                TextButton(onClick = onDontAskAgainClick) {
                    Text(
                        text = stringResource(Res.string.lbl_feedback_dont_ask_again),
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
private fun PreviewFeedbackChoiceScreen() {
    OrarUbbFmiTheme {
        FeedbackChoiceScreen(
            onFeedbackChoiceClick = {},
            onAskMeLaterClick = {},
            onDontAskAgainClick = {}
        )
    }
}
