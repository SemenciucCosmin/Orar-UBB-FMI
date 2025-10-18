package com.ubb.fmi.orar.data.students.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.students.datasource.StudyLinesDataSource
import com.ubb.fmi.orar.data.timetable.model.StudyLine
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class StudyLinesRepositoryImpl(
    private val coroutineScope: CoroutineScope,
    private val studyLinesDataSource: StudyLinesDataSource,
    private val timetablePreferences: TimetablePreferences,
) : StudyLinesRepository {

    private val studyLinesFlow: MutableStateFlow<Resource<List<StudyLine>>> = MutableStateFlow(
        Resource(null, Status.Loading)
    )

    init {
        prefetchStudyLines()
        initializeStudyLines()
    }

    override fun getStudyLines(): Flow<Resource<List<StudyLine>>> {
        return studyLinesFlow.map { resource ->
            val sortedStudyLines = resource.payload?.sortedBy { it.name }
            resource.copy(payload = sortedStudyLines)
        }
    }

    override suspend fun invalidate(year: Int, semesterId: String) {
        studyLinesDataSource.invalidate(year, semesterId)
    }

    private fun prefetchStudyLines() {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let { getStudyLinesFromApi(it.year, it.semesterId) }
        }
    }

    private fun initializeStudyLines() {
        coroutineScope.launch {
            timetablePreferences.getConfiguration().collectLatest { configuration ->
                if (configuration == null) {
                    studyLinesFlow.update { Resource(null, Status.NotFoundError) }
                    return@collectLatest
                }

                getStudyLinesFromCache(configuration.year, configuration.semesterId)
            }
        }
    }

    private suspend fun getStudyLinesFromCache(
        year: Int,
        semesterId: String,
    ) {
        studyLinesDataSource.getStudyLinesFromCache(year, semesterId).collectLatest { studyLines ->
            when {
                studyLines.isEmpty() -> getStudyLinesFromApi(year, semesterId)
                else -> studyLinesFlow.update { Resource(studyLines, Status.Success) }
            }
        }
    }

    private suspend fun getStudyLinesFromApi(
        year: Int,
        semesterId: String,
    ) {
        val resource = studyLinesDataSource.getStudyLinesFromApi(year, semesterId)
        val studyLine = resource.payload

        when {
            resource.status.isSuccess() && studyLine != null -> {
                studyLinesDataSource.saveStudyLinesInCache(studyLine)
            }

            else -> studyLinesFlow.update { Resource(null, resource.status) }
        }
    }
}