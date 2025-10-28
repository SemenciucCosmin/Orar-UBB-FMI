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
import androidx.compose.ui.text.withStyle
import com.ubb.fmi.orar.domain.extensions.DOT
import com.ubb.fmi.orar.domain.extensions.SPACE
import com.ubb.fmi.orar.domain.extensions.openUrl
import com.ubb.fmi.orar.ui.catalog.extensions.getContext
import com.ubb.fmi.orar.ui.catalog.extensions.showToast
import com.ubb.fmi.orar.ui.catalog.model.ToastLength
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_generic_error_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_github
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_open_source
import org.jetbrains.compose.resources.stringResource

private const val URL = "https://github.com/SemenciucCosmin/Orar-UBB-FMI"

@Composable
fun RepositoryHyperlinkText() {
    val context = getContext()
    val toastMessage = stringResource(Res.string.lbl_generic_error_message)

    val annotatedTextUrl = buildAnnotatedString {
        withStyle(SpanStyle(MaterialTheme.colorScheme.onBackground)) {
            append(stringResource(Res.string.lbl_open_source))
            append(String.SPACE)
        }

        pushLink(
            LinkAnnotation.Clickable(
                tag = stringResource(Res.string.lbl_github),
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

        append(stringResource(Res.string.lbl_github))

        pop()
        append(String.DOT)
    }

    Text(
        text = annotatedTextUrl,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.labelMedium
    )
}
