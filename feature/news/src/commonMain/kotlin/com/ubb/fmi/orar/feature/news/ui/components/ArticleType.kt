package com.ubb.fmi.orar.feature.news.ui.components

import com.ubb.fmi.orar.data.news.model.ArticleType
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_students
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_teachers
import org.jetbrains.compose.resources.StringResource

val ArticleType.labelRes: StringResource
    get() = when (this) {
        ArticleType.STUDENT -> Res.string.lbl_students
        ArticleType.TEACHER -> Res.string.lbl_teachers
    }
