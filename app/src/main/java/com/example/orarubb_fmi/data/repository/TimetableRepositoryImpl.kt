package com.example.orarubb_fmi.data.repository

import com.example.orarubb_fmi.common.END_HOUR
import com.example.orarubb_fmi.common.HEADLINE_TAG
import com.example.orarubb_fmi.common.HOURS_DELIMITER
import com.example.orarubb_fmi.common.HOUR_FORMAT
import com.example.orarubb_fmi.common.SPACE
import com.example.orarubb_fmi.common.START_HOUR
import com.example.orarubb_fmi.common.TABLE_COLUMN_TAG
import com.example.orarubb_fmi.common.TABLE_ROW_TAG
import com.example.orarubb_fmi.common.TABLE_TAG
import com.example.orarubb_fmi.common.toTimetableClass
import com.example.orarubb_fmi.common.toTimetableClassEntity
import com.example.orarubb_fmi.common.toTimetableInfo
import com.example.orarubb_fmi.common.toTimetableInfoEntity
import com.example.orarubb_fmi.data.datasource.api.TimetableApiService
import com.example.orarubb_fmi.data.datasource.dao.TimetableDao
import com.example.orarubb_fmi.data.network.CallResponse
import com.example.orarubb_fmi.domain.model.ClassType
import com.example.orarubb_fmi.domain.model.Participant
import com.example.orarubb_fmi.model.Timetable
import com.example.orarubb_fmi.model.TimetableClass
import com.example.orarubb_fmi.model.TimetableInfo
import com.example.orarubb_fmi.domain.model.Week
import com.example.orarubb_fmi.domain.repository.TimetableRepository
import com.example.orarubb_fmi.model.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.select.Elements
import java.io.IOException

class TimetableRepositoryImpl(
    private val timetableApiService: TimetableApiService,
    private val timetableDao: TimetableDao
) : TimetableRepository {

    override suspend fun getTimetable(timetableInfo: TimetableInfo): Resource<Timetable> {
        val cachedTimetable = getCachedTimetable()
        if (timetableInfo != cachedTimetable?.info) {
            val cloudResource = fetchTimetable(timetableInfo)
            val cloudTimetable = cloudResource.getOrNull() ?: return cloudResource
            reconcileCachedTimetable(cloudTimetable)
            return cloudResource
        } else {
            return Resource.Success(cachedTimetable)
        }
    }

    private suspend fun fetchTimetable(timetableInfo: TimetableInfo): Resource<Timetable> {
        val callResponse = safeApiCall(
            call = { fetchTimetableCall(timetableInfo) },
            errorMessage = "IO exception"
        )
        return when (callResponse) {
            is CallResponse.Error -> Resource.Error(Resource.ErrorType.Network)
            is CallResponse.Success -> {
                val cloudTimetable = parseCallResponse(timetableInfo, callResponse.data)
                if (cloudTimetable == null) {
                    Resource.Error(Resource.ErrorType.NotFound)
                } else {
                    Resource.Success(cloudTimetable)
                }
            }
        }
    }

    private suspend fun parseCallResponse(
        timetableInfo: TimetableInfo,
        callResponse: ResponseBody
    ): Timetable? {
        return withContext(Dispatchers.Default) {
            val document = Jsoup.parse(callResponse.string())
            val tables = document.select(TABLE_TAG)
            val headlineTags = document.select(HEADLINE_TAG)
            val groupElements = headlineTags.subList(1, headlineTags.size)
            val groups = groupElements.mapNotNull { element ->
                element.text().split(String.SPACE).lastOrNull()
            }

            val timetablesClasses = tables.mapIndexed { index, table ->
                val rows = table.select(TABLE_ROW_TAG)
                rows.mapNotNull { row ->
                    getTimetableClass(
                        group = groups[index],
                        columns = row.select(TABLE_COLUMN_TAG)
                    )
                }
            }.flatten().ifEmpty { return@withContext null }

            Timetable(
                info = timetableInfo,
                classes = timetablesClasses
            )
        }
    }

    private suspend fun getCachedTimetable(): Timetable? {
        return withContext(Dispatchers.IO) {
            val infoEntity = timetableDao.getTimetableInfo() ?: return@withContext null
            val classEntities = timetableDao.getTimetableClasses() ?: return@withContext null

            Timetable(
                info = infoEntity.toTimetableInfo(),
                classes = classEntities.map { it.toTimetableClass() }
            )
        }
    }

    private suspend fun reconcileCachedTimetable(timetable: Timetable) {
        withContext(Dispatchers.IO) {
            timetableDao.deleteTimetableInfo()
            timetableDao.deleteTimetableClasses()
            saveTimetable(timetable)
        }
    }

    private suspend fun saveTimetable(timetable: Timetable) {
        timetableDao.insertTimetableInfo(timetable.toTimetableInfoEntity())
        timetable.classes.forEach { timetableClass ->
            timetableDao.insertTimetableClass(timetableClass.toTimetableClassEntity())
        }
    }

    private fun getTimetableClass(group: String, columns: Elements): TimetableClass? {
        if (columns.isEmpty()) return null

        val dayElement = columns[0].text() ?: return null
        val hourIntervalElement = columns[1].text() ?: return null
        val weekElement = columns[2].text() ?: return null
        val placeElement = columns[3].text() ?: return null
        val participantElement = columns[4].text() ?: return null
        val classTypeElement = columns[5].text() ?: return null
        val disciplineElement = columns[6].text() ?: return null
        val professorElement = columns[7].text() ?: return null

        val hours = hourIntervalElement.split(HOURS_DELIMITER)
        val startHour = String.format(HOUR_FORMAT, hours[START_HOUR].toInt())
        val endHour = String.format(HOUR_FORMAT, hours[END_HOUR].toInt())

        val week = Week.getWeekType(weekElement)
        val participant = Participant.getParticipantType(participantElement)
        val classType = ClassType.getClassType(classTypeElement)

        return TimetableClass(
            group = group,
            day = dayElement,
            startHour = startHour,
            endHour = endHour,
            week = week,
            place = placeElement,
            participant = participant,
            classType = classType,
            discipline = disciplineElement,
            professor = professorElement
        )
    }

    private suspend fun fetchTimetableCall(timetableInfo: TimetableInfo): CallResponse<ResponseBody> {
        val response = timetableApiService.getTimetablesHtml(
            year = timetableInfo.year,
            semester = timetableInfo.semester,
            studyField = timetableInfo.studyField.notation,
            studyLanguage = timetableInfo.studyLanguage.notation,
            studyYear = timetableInfo.studyYear
        )
        return if (!response.isSuccessful) {
            CallResponse.Error(Exception(response.errorBody().toString()))
        } else {
            val body = response.body()
            if (body == null) {
                CallResponse.Error(Exception("Response body cannot be null."))
            } else {
                CallResponse.Success(body)
            }
        }
    }

    private suspend fun <T : Any> safeApiCall(
        call: suspend () -> CallResponse<T>,
        errorMessage: String
    ): CallResponse<T> = try {
        call.invoke()
    } catch (e: Exception) {
        CallResponse.Error(IOException(errorMessage, e))
    }
}
