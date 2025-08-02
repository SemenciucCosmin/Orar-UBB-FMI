package com.ubb.fmi.orar.data.model

enum class StudyLine(val id: String) {
    // LICENCE

    // Mathematics Romanian
    M1(id = "M1"),
    M2(id = "M2"),
    M3(id = "M3"),

    // Computer Science Romanian
    I1(id = "I1"),
    I2(id = "I2"),
    I3(id = "I3"),

    // Mathematics-Computer Science Romanian
    MI1(id = "MI1"),
    MI2(id = "MI2"),
    MI3(id = "MI3"),

    // Mathematics-Computer Science English
    MIE1(id = "MIE1"),
    MIE2(id = "MIE2"),
    MIE3(id = "MIE3"),

    // Mathematics Hungarian
    MM1(id = "MM1"),
    MM2(id = "MM2"),
    MM3(id = "MM3"),

    // Computer Science Hungarian
    IM1(id = "IM1"),
    IM2(id = "IM2"),
    IM3(id = "IM3"),

    // Mathematics-Computer Science Hungarian
    MIM1(id = "MIM1"),
    MIM2(id = "MIM2"),
    MIM3(id = "MIM3"),

    // Computer Science Engineering Hungarian
    IIM1(id = "IIM1"),
    IIM2(id = "IIM2"),

    // Computer Science German
    IG1(id = "IG1"),
    IG2(id = "IG2"),
    IG3(id = "IG3"),

    // Computer Science English
    IE1(id = "IE1"),
    IE2(id = "IE2"),
    IE3(id = "IE3"),

    // Artificial Intelligence Romanian
    IA1(id = "IA1"),
    IA2(id = "IA2"),

    // Information Engineering Romanian
    II1(id = "II1"),
    II2(id = "II2"),

    // Psychology Romanian
    PSIHO(id = "Psiho"),

    // MASTER

    // Modern Methods in Teaching Mathematics Romanian
    MET_MOD_DID1(id = "MaMetModDid1"),
    MET_MOD_DID2(id = "MaMetModDid2"),

    // Advanced Mathematics English
    V1(id = "MaMAv1"),
    V2(id = "MaMAv2"),

    // Modern Methods in Teaching Mathematics Hungarian
    MET_MOD_DID_M1(id = "MaMetModDidm1"),
    MET_MOD_DID_M2(id = "MaMetModDidm2"),

    // Databases Romanian
    BD1(id = "MaBD1"),
    BD2(id = "MaBD2"),

    // Network Distributed Systems Romanian
    SD1(id = "MaSD1"),
    SD2(id = "MaSD2"),

    // Software Engineering English
    IS1(id = "MaIS1"),
    IS2(id = "MaIS2"),

    // Applied Computational Intelligence English
    ICA1(id = "MaICA1"),
    ICA2(id = "MaICA2"),

    // High Performance Calculus Romanian
    CIP1(id = "MaCIP1"),
    CIP2(id = "MaCIP2"),

    // Advanced Computer Science Systems German-English
    SIA1(id = "MaSIA1"),
    SIA2(id = "MaSIA2"),

    // Data Science in Industry and Society Romanian
    DATA_SCI1(id = "MaDataSci1"),
    DATA_SCI2(id = "MaDataSci2"),

    // Cyber Security Romanian
    CYBER1(id = "Cyber"),
    CYBER2(id = "Cyber"),

    // Data Analysis and Modeling Hungarian
    ADM1(id = "MaADM1"),
    ADM2(id = "MaADM2"),

    // Enterprise Application Design and Development Romanian
    PDAE1(id = "MaPDAE1"),
    PDAE2(id = "MaPDAE2"),

    // Didactic Master in Computer Science Romanian
    ST_EDU_ID1(id = "StEduID1"),
    ST_EDU_ID2(id = "StEduID2"),

    // Didactic Master in Mathematics Hungarian
    ST_EDU_MD_M1(id = "StEduMDm1"),
    ST_EDU_MD_M2(id = "StEduMDm2"),

    // Didactic Master in Computer Science Hungarian
    ST_EDU_ID_M1(id = "StEduIDm1"),
    ST_EDU_ID_M2(id = "StEduIDm2");

    companion object {
        fun getById(id: String): StudyLine {
            return entries.firstOrNull { it.id == id } ?: M1
        }
    }
}