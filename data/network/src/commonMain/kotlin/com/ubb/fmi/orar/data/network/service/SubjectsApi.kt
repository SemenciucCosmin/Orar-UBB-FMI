package com.ubb.fmi.orar.data.network.service

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

/**
 * Api service for fetching subject related information
 */
class SubjectsApi(private val httpClient: HttpClient) {

    /**
     * Fetch study lines table in html format by [year] and [semesterId]
     */
    suspend fun getSubjectsHtml(
        year: Int,
        semesterId: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("$BASE_URL/$year-$semesterId/disc/index.html")
        }
    }

    /**
     * Fetch subject events in html format by [year], [semesterId] and [subjectId]
     */
    suspend fun getEventsHtml(
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