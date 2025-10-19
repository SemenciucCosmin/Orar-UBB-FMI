package com.ubb.fmi.orar.data.studylines.repository

import com.ubb.fmi.orar.data.network.model.Resource
import com.ubb.fmi.orar.data.network.model.Status
import com.ubb.fmi.orar.data.network.model.isSuccess
import com.ubb.fmi.orar.data.studylines.datasource.StudyLinesDataSource
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

/**
 * Repository for managing data flow and data source for study lines
 */
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

    /**
     * Retrieves a [Flow] of study lines
     */
    override fun getStudyLines(): Flow<Resource<List<StudyLine>>> {
        return studyLinesFlow.map { resource ->
            val sortedStudyLines = resource.payload?.sortedBy { it.name }
            resource.copy(payload = sortedStudyLines)
        }
    }

    /**
     * Invalidates study lines cache
     */
    override suspend fun invalidate(year: Int, semesterId: String) {
        studyLinesDataSource.invalidate(year, semesterId)
    }

    /**
     * Tries prefetching the study lines from API for a safety update of local data
     */
    private fun prefetchStudyLines() {
        coroutineScope.launch {
            val configuration = timetablePreferences.getConfiguration().firstOrNull()
            configuration?.let { getStudyLinesFromApi(it.year, it.semesterId) }
        }
    }

    /**
     * Initializes collection of database entries and possible API updates
     */
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

    /**
     * Provides the collection of database data flow
     */
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

    /**
     * Retrieves study lines from API and update the database of output flow
     */
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