package com.ubb.fmi.orar.ui.catalog.components.custom

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import com.ubb.fmi.orar.domain.extensions.ASTERISK

@Composable
fun OutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String? = null,
    placeholder: String? = null,
    supportingText: String? = null,
    readOnly: Boolean = false,
    isError: Boolean = false,
    isMandatory: Boolean = false,
    singleLine: Boolean = false,
    maxLines: Int = if (singleLine) 1 else Int.MAX_VALUE,
    minLines: Int = 1,
    shape: Shape = MaterialTheme.shapes.large,
    colors: TextFieldColors = OutlinedTextFieldDefaults.colors(),
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label = label?.let {
            {
                Text(text = if (isMandatory) label + String.ASTERISK else label)
            }
        },
        placeholder = placeholder?.let {
            {
                Text(text = if (isMandatory) placeholder + String.ASTERISK else placeholder)
            }
        },
        supportingText = supportingText?.let {
            {
                Text(text = supportingText)
            }
        },
        readOnly = readOnly,
        isError = isError,
        singleLine = singleLine,
        maxLines = maxLines,
        minLines = minLines,
        shape = shape,
        colors = colors
    )
}