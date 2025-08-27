package com.ubb.fmi.orar.data.studyline.api

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class StudyLineApi(private val httpClient: HttpClient) {

    suspend fun getStudyLines(
        year: Int,
        semesterId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/tabelar/index.html")
        }
    }

    suspend fun getStudyLineTimetable(
        year: Int,
        semesterId: String,
        studyLineId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/tabelar/$studyLineId.html")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}
