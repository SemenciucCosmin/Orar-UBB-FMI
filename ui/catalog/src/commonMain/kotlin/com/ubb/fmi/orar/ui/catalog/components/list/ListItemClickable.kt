package com.ubb.fmi.orar.ui.catalog.components.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_group
import orar_ubb_fmi.ui.catalog.generated.resources.ic_location
import orar_ubb_fmi.ui.catalog.generated.resources.ic_right_arrow
import orar_ubb_fmi.ui.catalog.generated.resources.ic_teacher
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ListItemClickable(
    headline: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    headlineTextStyle: TextStyle = MaterialTheme.typography.titleSmall,
    overline: String? = null,
    underLine: String? = null,
    leadingIcon: Painter? = null,
    trailingIconSize: Dp = Pds.icon.Medium,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        ListRow(
            modifier = Modifier.padding(Pds.spacing.SMedium),
            headline = headline,
            textAlign = textAlign,
            overline = overline,
            underLine = underLine,
            leadingIcon = leadingIcon,
            trailingIconSize = trailingIconSize,
            headlineTextStyle = headlineTextStyle,
            trailingIcon = painterResource(Res.drawable.ic_right_arrow),
        )
    }
}

@Preview
@Composable
private fun PreviewListItemClickableV1() {
    OrarUbbFmiTheme {
        ListItemClickable(
            headline = "Headline",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewListItemClickableV2() {
    OrarUbbFmiTheme {
        ListItemClickable(
            headline = "Headline",
            leadingIcon = painterResource(Res.drawable.ic_group),
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewListItemClickableV3() {
    OrarUbbFmiTheme {
        ListItemClickable(
            headline = "Headline",
            overline = "Overline",
            leadingIcon = painterResource(Res.drawable.ic_teacher),
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PreviewListItemClickableV4() {
    OrarUbbFmiTheme {
        ListItemClickable(
            headline = "Headline",
            underLine = "Underline",
            textAlign = TextAlign.Center,
            leadingIcon = painterResource(Res.drawable.ic_location),
            onClick = {},
        )
    }
}
