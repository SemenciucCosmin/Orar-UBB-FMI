package com.example.orarubb_fmi.data.datasource.api

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface TimetableApiService {
    @GET("{year}-{semester}/tabelar/{studyField}{studyLanguage}{studyYear}.html")
    suspend fun getTimetablesHtml(
        @Path("year") year: Int,
        @Path("semester") semester: Int,
        @Path("studyField") studyField: String,
        @Path("studyLanguage") studyLanguage: String,
        @Path("studyYear") studyYear: Int
    ): ResponseBody
}
