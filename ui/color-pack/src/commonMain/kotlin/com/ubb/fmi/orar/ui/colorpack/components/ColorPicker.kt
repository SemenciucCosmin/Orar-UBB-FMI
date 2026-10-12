package com.ubb.fmi.orar.ui.colorpack.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.ui.colorpack.model.HsvColor
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds

/**
 * Complete color picker: saturation/value panel, hue slider, preset swatches and hex input.
 *
 * @param color The currently selected color.
 * @param onColorChange Callback with every newly selected color.
 * @param modifier Modifier to be applied to the picker.
 * @param swatches Preset colors offered as quick picks.
 */
@Composable
fun ColorPicker(
    color: Color,
    onColorChange: (Color) -> Unit,
    modifier: Modifier = Modifier,
    swatches: List<Color> = DefaultSwatches,
) {
    // Kept as HSV so the hue survives when saturation or value drop to zero.
    var hsvColor by remember { mutableStateOf(HsvColor.from(color)) }

    LaunchedEffect(color) {
        if (hsvColor.toColor() != color) hsvColor = HsvColor.from(color)
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
    ) {
        SaturationValuePanel(
            hsvColor = hsvColor,
            onChange = { saturation, value ->
                hsvColor = hsvColor.copy(saturation = saturation, value = value)
                onColorChange(hsvColor.copy(saturation = saturation, value = value).toColor())
            },
        )

        HueSlider(
            hue = hsvColor.hue,
            onHueChange = { hue ->
                hsvColor = hsvColor.copy(hue = hue)
                onColorChange(hsvColor.copy(hue = hue).toColor())
            },
        )

        ColorSwatches(
            swatches = swatches,
            selectedColor = color,
            onSwatchClick = onColorChange,
        )

        HexColorTextField(
            color = color,
            onColorChange = onColorChange,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Preview
@Composable
private fun PreviewColorPicker() {
    OrarUbbFmiTheme {
        ColorPicker(color = Color(0xFF2563EB), onColorChange = {})
    }
}
