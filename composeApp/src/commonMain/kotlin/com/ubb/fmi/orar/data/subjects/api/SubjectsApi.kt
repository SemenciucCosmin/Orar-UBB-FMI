package com.ubb.fmi.orar.data.subjects.api

import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class SubjectsApi(private val httpClient: HttpClient) {
    suspend fun getSubjectsMapHtml(
        year: Int,
        semesterId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/disc/index.html")
        }
    }

    suspend fun getSubjectTimetableHtml(
        year: Int,
        semesterId: String,
        subjectId: String
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/disc/$subjectId.html")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}
