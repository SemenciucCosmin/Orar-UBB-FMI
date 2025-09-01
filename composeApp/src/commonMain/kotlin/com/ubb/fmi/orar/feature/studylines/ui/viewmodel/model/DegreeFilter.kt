package com.ubb.fmi.orar.feature.studylines.ui.viewmodel.model

import com.ubb.fmi.orar.data.groups.model.Degree
import orar_ubb_fmi.composeapp.generated.resources.Res
import orar_ubb_fmi.composeapp.generated.resources.lbl_all
import orar_ubb_fmi.composeapp.generated.resources.lbl_license
import orar_ubb_fmi.composeapp.generated.resources.lbl_master
import org.jetbrains.compose.resources.StringResource

enum class DegreeFilter(
    val id: String,
    val labelRes: StringResource,
    val orderIndex: Int,
) {
    ALL(
        id = "all",
        labelRes = Res.string.lbl_all,
        orderIndex = 0
    ),
    LICENCE(
        id = Degree.LICENCE.id,
        labelRes = Res.string.lbl_license,
        orderIndex = 1
    ),
    MASTER(
        id = Degree.MASTER.id,
        labelRes = Res.string.lbl_master,
        orderIndex = 2
    );

    companion object {
        fun getById(id: String?): DegreeFilter {
            return entries.firstOrNull { it.id == id } ?: ALL
        }
    }
}
