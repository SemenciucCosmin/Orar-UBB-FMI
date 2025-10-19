package com.ubb.fmi.orar.data.network.service

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Api service for fetching students related information
 */
class StudentsApi(private val httpClient: HttpClient) {

    /**
     * Fetch study line table in html format by [year] and [semesterId]
     */
    suspend fun getStudyLines(
        year: Int,
        semesterId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/tabelar/index.html")
        }
    }

    /**
     * Fetch study line events in html format by [year], [semesterId] and [studyLineId]
     */
    suspend fun getEventsHtml(
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