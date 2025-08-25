package com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model

import com.ubb.fmi.orar.data.core.model.Degree

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
        label = Degree.LICENCE.label,
        orderIndex = 1
    ),
    MASTER(
        id = Degree.MASTER.id,
        label = Degree.MASTER.label,
        orderIndex = 2
    );
}
