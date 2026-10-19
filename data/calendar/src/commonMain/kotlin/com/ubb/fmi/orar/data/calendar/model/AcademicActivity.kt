package com.ubb.fmi.orar.data.calendar.model

enum class AcademicActivity(val id: String) {
    ACADEMIC_PREPARATION("pregătirea anului universitar"),
    TEACHING_ACTIVITY("activitate didactică"),
    CHRISTMAS_HOLIDAY("vacanță de Crăciun"),
    EASTER_HOLIDAY("vacanța de Paști"),
    EXAM_SESSION("sesiune de examene"),
    RETAKE_SESSION("sesiune de restanțe"),
    INTERNSHIP_PERIOD("perioada de practică"),
    THESIS_PREPARATION("pregătirea examenului de licență/disertație"),
    THESIS_EXAM("examen de licență/disertație"),
    HOLIDAY("vacanță");

    companion object {
        fun getById(id: String): AcademicActivity? {
            return entries.find { it.id == id }
        }
    }
}