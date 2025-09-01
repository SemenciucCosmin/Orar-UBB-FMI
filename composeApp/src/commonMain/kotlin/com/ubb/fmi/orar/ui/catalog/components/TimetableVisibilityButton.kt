package com.ubb.fmi.orar.ui.catalog.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_hide
import orar_ubb_fmi.composeapp.generated.resources.ic_show
import org.jetbrains.compose.resources.painterResource

@Composable
fun TimetableVisibilityButton(
    isVisible: Boolean,
    onClick: () -> Unit,
) {
    IconToggleButton(
        checked = isVisible,
        onCheckedChange = { onClick() },
    ) {
        Icon(
            modifier = Modifier.size(Pds.icon.SMedium),
            contentDescription = null,
            painter = when {
                isVisible -> painterResource(Res.drawable.ic_show)
                else -> painterResource(Res.drawable.ic_hide)
            }
        )
    }
}
