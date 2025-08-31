package com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model

import com.ubb.fmi.orar.data.groups.model.Degree

enum class DegreeFilter(
    val id: String,
    val label: String,
    val orderIndex: Int,
) {
    ALL(
        id = "all",
        label = "All",
        orderIndex = 0
    ),
    LICENCE(
        id = Degree.LICENCE.id,
        label = "Licenta",
        orderIndex = 1
    ),
    MASTER(
        id = Degree.MASTER.id,
        label = "Master",
        orderIndex = 2
    );

    companion object {
        fun getById(id: String?): DegreeFilter {
            return entries.firstOrNull { it.id == id } ?: ALL
        }
    }
}
