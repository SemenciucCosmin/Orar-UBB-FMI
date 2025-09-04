package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * A composable that displays a primary button with customizable properties.
 *
 * @param text The text to be displayed on the button.
 * @param onClick Callback invoked when the button is clicked.
 * @param modifier Modifier to be applied to the button.
 * @param enabled Whether the button is enabled or not.
 * @param shape The shape of the button.
 * @param colors The colors used for the button.
 * @param elevation The elevation of the button.
 * @param border The border of the button, if any.
 * @param contentPadding Padding values for the content inside the button.
 * @param interactionSource The interaction source for this button, used for handling interactions.
 */
@Composable
fun PrimaryButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = MaterialTheme.shapes.small,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource? = null,
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
private fun PreviewPrimaryButton() {
    OrarUbbFmiTheme {
        PrimaryButton(
            text = "Next",
            onClick = {}
        )
    }
}
