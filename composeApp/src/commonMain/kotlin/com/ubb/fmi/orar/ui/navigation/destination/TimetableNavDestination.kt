package com.ubb.fmi.orar.ui.navigation.destination

import kotlinx.serialization.Serializable

@Serializable
sealed class TimetableNavDestination() {

    @Serializable
    data object Main: TimetableNavDestination()
}
