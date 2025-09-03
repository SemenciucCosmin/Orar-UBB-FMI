package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_all
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_license
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_master
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
        id = "licenta",
        labelRes = Res.string.lbl_license,
        orderIndex = 1
    ),
    MASTER(
        id = "master",
        labelRes = Res.string.lbl_master,
        orderIndex = 2
    )
}