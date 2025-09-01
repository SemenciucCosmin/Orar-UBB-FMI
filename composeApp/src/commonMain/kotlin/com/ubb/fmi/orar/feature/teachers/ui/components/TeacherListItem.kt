package com.ubb.fmi.orar.feature.teachers.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.ic_right_arrow
import orar_ubb_fmi.composeapp.generated.resources.ic_teacher
import org.jetbrains.compose.resources.painterResource

@Composable
fun TeacherListItem(
    title: String,
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(Pds.spacing.SMedium),
            horizontalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(Pds.icon.SMedium),
                painter = painterResource(Res.drawable.ic_teacher),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(Pds.spacing.XXSmall),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Icon(
                modifier = Modifier.size(Pds.icon.Medium),
                painter = painterResource(Res.drawable.ic_right_arrow),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
