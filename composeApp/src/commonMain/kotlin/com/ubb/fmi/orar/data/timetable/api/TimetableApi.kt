package com.ubb.fmi.orar.data.timetable.api

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class TimetableApi(private val httpClient: HttpClient) {
    suspend fun getTimetablesHtml(
        year: String,
        semester: String,
        studyField: String,
        studyLanguage: String,
        studyYear: String
    ){
        httpClient.get("$BASE_URL/$year-$semester/tabelar/$studyField$studyLanguage$studyYear.html") {

        }
    }


    suspend fun getTimetable(): String {
        return httpClient.get(
            "https://www.cs.ubbcluj.ro/files/orar/2024-2/tabelar/M1.html"
        ).bodyAsText()
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}
