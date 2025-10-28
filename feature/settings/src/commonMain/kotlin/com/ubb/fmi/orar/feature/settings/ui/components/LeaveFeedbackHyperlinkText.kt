package com.ubb.fmi.orar.feature.settings.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import com.ubb.fmi.orar.domain.extensions.openUrl
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_generic_error_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_leave_feedback
import org.jetbrains.compose.resources.stringResource

private const val URL = "https://forms.gle/T1ZK6aB3NCVphqfZ8"

@Composable
fun LeaveFeedbackHyperlinkText() {
    val context = getContext()
    val toastMessage = stringResource(Res.string.lbl_generic_error_message)

    val annotatedTextUrl = buildAnnotatedString {
        pushLink(
            LinkAnnotation.Clickable(
                tag = stringResource(Res.string.lbl_leave_feedback),
                linkInteractionListener = {
                    openUrl(URL, context) {
                        showToast(context, toastMessage, ToastLength.SHORT)
                    }
                },
                styles = TextLinkStyles(
                    SpanStyle(
                        color = MaterialTheme.colorScheme.onBackground,
                        textDecoration = TextDecoration.Underline
                    )
                )
            )
        )

        append(stringResource(Res.string.lbl_leave_feedback))
    }

    Text(
        text = annotatedTextUrl,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium
    )
}
