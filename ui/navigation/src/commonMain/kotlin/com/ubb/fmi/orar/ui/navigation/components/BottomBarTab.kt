package com.ubb.fmi.orar.ui.navigation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.ubb.fmi.orar.ui.theme.Pds

/**
 * Tab of the [BottomBar]. Only the icon is displayed, unless selected, when it expands into a
 * colored pill revealing the [label] as well.
 */
@Composable
fun BottomBarTab(
    icon: Painter,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val colorScheme = MaterialTheme.colorScheme
    val containerColor by animateColorAsState(
        targetValue = if (isSelected) colorScheme.primaryContainer else colorScheme.surfaceContainer,
        label = "bottomBarTabContainer"
    )
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) colorScheme.onPrimaryContainer else colorScheme.onSurfaceVariant,
        label = "bottomBarTabContent"
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .clip(CircleShape)
            .background(containerColor)
            .selectable(selected = isSelected, onClick = onClick, role = Role.Tab)
            .padding(horizontal = Pds.spacing.Medium, vertical = Pds.spacing.SMedium)
    ) {
        Icon(
            painter = icon,
            contentDescription = label.takeUnless { isSelected },
            tint = contentColor,
            modifier = Modifier.size(Pds.icon.SMedium)
        )

        AnimatedVisibility(
            visible = isSelected,
            enter = fadeIn() + expandHorizontally(),
            exit = fadeOut() + shrinkHorizontally(),
        ) {
            Text(
                text = label,
                color = contentColor,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = Pds.spacing.Small)
            )
        }
    }
}
