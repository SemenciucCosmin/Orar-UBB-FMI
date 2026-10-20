package com.ubb.fmi.orar.feature.explore.ui.route

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.ubb.fmi.orar.ui.catalog.calculateContentBottomPadding
import com.ubb.fmi.orar.ui.catalog.components.NavigationCard
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.navigation.destination.ExploreNavDestination
import com.ubb.fmi.orar.ui.theme.Pds
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.ic_building
import orar_ubb_fmi.ui.catalog.generated.resources.ic_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.ic_custom
import orar_ubb_fmi.ui.catalog.generated.resources.ic_location
import orar_ubb_fmi.ui.catalog.generated.resources.ic_student
import orar_ubb_fmi.ui.catalog.generated.resources.ic_subject
import orar_ubb_fmi.ui.catalog.generated.resources.ic_teacher
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_add_custom_event
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_add_custom_event_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_explore
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_free_rooms_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_rooms
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_rooms_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_students
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_students_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_subjects
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_subjects_details
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_teachers
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_teachers_details
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ExploreRoute(navController: NavController) {
    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_explore),
                titleStyle = MaterialTheme.typography.headlineMedium
            )
        }
    ) { paddingValues ->
        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.SMedium),
            modifier = Modifier
                .padding(top = paddingValues.calculateTopPadding())
                .verticalScroll(rememberScrollState())
                .padding(Pds.spacing.Medium)
                .padding(bottom = paddingValues.calculateContentBottomPadding())
        ) {
            NavigationCard(
                title = stringResource(Res.string.lbl_students),
                text = stringResource(Res.string.lbl_students_details),
                painter = painterResource(Res.drawable.ic_student),
                onClick = { navController.navigate(ExploreNavDestination.StudyLines) },
            )

            NavigationCard(
                title = stringResource(Res.string.lbl_teachers),
                text = stringResource(Res.string.lbl_teachers_details),
                painter = painterResource(Res.drawable.ic_teacher),
                onClick = { navController.navigate(ExploreNavDestination.Teachers) },
            )

            NavigationCard(
                title = stringResource(Res.string.lbl_subjects),
                text = stringResource(Res.string.lbl_subjects_details),
                painter = painterResource(Res.drawable.ic_subject),
                onClick = { navController.navigate(ExploreNavDestination.Subjects) },
            )

            NavigationCard(
                title = stringResource(Res.string.lbl_rooms),
                text = stringResource(Res.string.lbl_rooms_details),
                painter = painterResource(Res.drawable.ic_building),
                onClick = { navController.navigate(ExploreNavDestination.Rooms) },
            )

            NavigationCard(
                title = stringResource(Res.string.lbl_free_rooms),
                text = stringResource(Res.string.lbl_free_rooms_details),
                painter = painterResource(Res.drawable.ic_location),
                onClick = { navController.navigate(ExploreNavDestination.FreeRooms) },
            )

            NavigationCard(
                title = stringResource(Res.string.lbl_add_custom_event),
                text = stringResource(Res.string.lbl_add_custom_event_details),
                painter = painterResource(Res.drawable.ic_custom),
                onClick = { navController.navigate(ExploreNavDestination.AddCustomEvent) },
            )

            NavigationCard(
                title = stringResource(Res.string.lbl_calendar),
                text = stringResource(Res.string.lbl_calendar_details),
                painter = painterResource(Res.drawable.ic_calendar),
                onClick = { navController.navigate(ExploreNavDestination.Calendar) },
            )
        }
    }
}
