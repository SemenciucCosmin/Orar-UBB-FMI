package com.ubb.fmi.orar.data.studyline.api

import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse

class StudyLineApi(private val httpClient: HttpClient) {

    suspend fun getStudyLineTimetable(
        year: Int,
        semester: String,
        studyLineId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semester/tabelar/$studyLineId.html")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}
