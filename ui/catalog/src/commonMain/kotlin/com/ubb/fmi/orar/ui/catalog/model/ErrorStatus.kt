package com.ubb.fmi.orar.ui.catalog.model

import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_generic_error_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_network_error_message
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_not_found_error_message
import org.jetbrains.compose.resources.StringResource

enum class ErrorStatus(val labelRes: StringResource) {
    GENERIC(labelRes = Res.string.lbl_generic_error_message),
    NOT_FOUND(labelRes = Res.string.lbl_not_found_error_message),
    NETWORK(labelRes = Res.string.lbl_network_error_message),
}
