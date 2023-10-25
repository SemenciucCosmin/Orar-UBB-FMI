package com.example.orarubb_fmi.data.datasource.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface TimetableApiService {
    @GET("{year}-{semester}/tabelar/{studyField}{studyLanguage}{studyYear}.html")
    suspend fun getTimetablesHtml(
        @Path("year") year: String,
        @Path("semester") semester: String,
        @Path("studyField") studyField: String,
        @Path("studyLanguage") studyLanguage: String,
        @Path("studyYear") studyYear: String
    ): ResponseBody
}
