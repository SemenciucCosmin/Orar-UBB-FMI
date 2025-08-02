package com.ubb.fmi.orar.data.teachers.api

import com.ubb.fmi.orar.network.model.Resource
import com.ubb.fmi.orar.network.model.processApiResource
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class TeachersApi(private val httpClient: HttpClient) {
    suspend fun getTeachersHtml(
        year: Int,
        semester: String,
    ): Resource<String> {
        return processApiResource {
            httpClient.get("${BASE_URL}/$year-$semester/cadre/index.html")
        }
    }

    suspend fun getTeacherTimetableHtml(
        year: Int,
        semester: String,
        teacherId: String
    ): Resource<String> {
        return processApiResource {
            httpClient.get("${BASE_URL}/$year-$semester/cadre/$teacherId.html")
        }
    }

    companion object {
        private const val BASE_URL = "https://www.cs.ubbcluj.ro/files/orar"
    }
}
