package com.ubb.fmi.orar.data.timetable.model

enum class Field(
    val id: String,
    val degree: Degree,
    val studyLanguages: List<StudyLanguage>,
) {
    M(
        id = "M",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    I(
        id = "I",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MI(
        id = "MI",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MI_DS(
        id = "MIDS",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MI_E(
        id = "MIE",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    M_M(
        id = "MM",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    I_M(
        id = "IM",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    MI_M_DS(
        id = "MIMDS",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    MI_M(
        id = "MIM",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    II_M(
        id = "IIM",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    I_E(
        id = "IE",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    I_G(
        id = "IG",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.GERMAN),
    ),
    II(
        id = "II",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    IA(
        id = "IA",
        degree = Degree.LICENCE,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),

    MA_MET_MOD_DID(
        id = "MaMetModDid",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MA_M_AV(
        id = "MaMAv",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    MA_MET_MOD_DID_M(
        id = "MaMetModDidm",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    MA_BD(
        id = "MaBD",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MA_SD(
        id = "MaSD",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MA_IS(
        id = "MaIS",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    MA_I_C_A(
        id = "MaICA",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    MA_C_I_P(
        id = "MaCIP",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    MA_S_I_A(
        id = "MaSIA",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.GERMAN, StudyLanguage.HUNGARIAN),
    ),
    MA_DATA_SCI(
        id = "MaDataSci",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    Cyber(
        id = "Cyber",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MA_A_D_M(
        id = "MaADM",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    P_D_E_A(
        id = "PDEA",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    MA_A_I_4_C_I(
        id = "MaAI4CI",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    C_N_I_H_P_C(
        id = "CNIHPC",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ENGLISH),
    ),
    ST_EDU_I_D(
        id = "StEduID",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.ROMANIAN),
    ),
    ST_EDU_M_DM(
        id = "StEduMDm",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    ),
    ST_EDU_I_DM(
        id = "StEduIDm",
        degree = Degree.MASTER,
        studyLanguages = listOf(StudyLanguage.HUNGARIAN),
    );

    companion object {
        fun getById(id: String): Field? {
            return entries.find { it.id == id }
        }
    }
}
