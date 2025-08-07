package com.ubb.fmi.orar.feature.form.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ubb.fmi.orar.data.teachers.model.Teacher
import com.ubb.fmi.orar.data.teachers.model.TeacherTitle
import com.ubb.fmi.orar.ui.catalog.extensions.conditional
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import kotlinx.datetime.format.Padding
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.check
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TeachersFormList(
    teachers: List<Teacher>,
    selectedTeacherId: String?,
    onTeacherClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
) {
    LazyColumn(
        contentPadding = contentPadding,
        modifier = modifier.background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(teachers) { teacher ->
            ElevatedCard(
                onClick = { onTeacherClick(teacher.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .conditional(selectedTeacherId == teacher.id) {
                        border(
                            1.dp,
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.shapes.medium
                        )
                    }
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = teacher.titleId,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onBackground
                        )

                        Text(
                            text = teacher.name,
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }

                    if (selectedTeacherId == teacher.id) {
                        Icon(
                            painter = painterResource(Res.drawable.check),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewTeachersFormList() {
    OrarUbbFmiTheme {
        TeachersFormList(
            selectedTeacherId = "4",
            onTeacherClick = {},
            teachers = List(10) {
                Teacher(
                    id = it.toString(),
                    name = "Name $it",
                    titleId = TeacherTitle.entries.random().id
                )
            }
        )
    }
}
