package com.ubb.fmi.orar.ui.colorpack.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.domain.extensions.HASH
import com.ubb.fmi.orar.ui.colorpack.extensions.toColorOrNull
import com.ubb.fmi.orar.ui.colorpack.extensions.toHex
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import orar_ubb_fmi.ui.color_pack.generated.resources.Res
import orar_ubb_fmi.ui.color_pack.generated.resources.lbl_hex_code
import orar_ubb_fmi.ui.color_pack.generated.resources.lbl_invalid_hex_code
import org.jetbrains.compose.resources.stringResource

private const val MAX_HEX_INPUT_LENGTH = 7
private const val HEX_DIGITS = "0123456789ABCDEF"

/**
 * Text field for typing a color as a "#RRGGBB" hex code. Valid input is reported through
 * [onColorChange]; external changes of [color] are reflected in the field.
 *
 * @param color The currently selected color.
 * @param onColorChange Callback with the parsed color whenever the input is a valid hex code.
 * @param modifier Modifier to be applied to the text field.
 */
@Composable
fun HexColorTextField(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
) {
    var text by remember { mutableStateOf(color.toHex()) }
    val parsedColor = text.toColorOrNull()

    LaunchedEffect(color) {
        if (text.toColorOrNull() != color) text = color.toHex()
    }

    OutlinedTextField(
        value = text,
        onValueChange = { input ->
            val sanitized = String.HASH + input.uppercase().filter { it in HEX_DIGITS }
            text = sanitized.take(MAX_HEX_INPUT_LENGTH)
            text.toColorOrNull()?.let(onColorChange)
        },
        modifier = modifier,
        label = { Text(text = stringResource(Res.string.lbl_hex_code)) },
        supportingText = when (parsedColor) {
            null -> {
                { Text(text = stringResource(Res.string.lbl_invalid_hex_code)) }
            }

            else -> null
        },
        isError = parsedColor == null,
        singleLine = true,
        shape = MaterialTheme.shapes.large,
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            autoCorrectEnabled = false,
            imeAction = ImeAction.Done,
        ),
    )
}

@Preview
@Composable
private fun PreviewHexColorTextField() {
    OrarUbbFmiTheme {
        HexColorTextField(color = Color.Red, onColorChange = {})
    }
}
