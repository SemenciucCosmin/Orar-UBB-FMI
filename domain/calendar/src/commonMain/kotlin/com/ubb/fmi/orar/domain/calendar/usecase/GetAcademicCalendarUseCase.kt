package com.ubb.fmi.orar.domain.calendar.usecase

import com.ubb.fmi.orar.data.calendar.model.AcademicCalendar
import com.ubb.fmi.orar.data.calendar.repository.AcademicCalendarRepository
import com.ubb.fmi.orar.data.network.model.isLoading
import com.ubb.fmi.orar.data.studylines.repository.StudyLinesRepository
import com.ubb.fmi.orar.data.timetable.model.Field
import com.ubb.fmi.orar.data.timetable.model.StudyLanguage
import com.ubb.fmi.orar.data.timetable.preferences.TimetablePreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNot

/**
 * Provides the official [AcademicCalendar] that applies to the configured user, or `null` when
 * the calendar is unavailable.
 *
 * The official calendar publishes several variants of each semester, by study language and for
 * final year students. Only the variant matching the user is kept, so the returned calendar has
 * a single semester per [com.ubb.fmi.orar.data.timetable.model.Semester]:
 *  - the study language is the first language of the configured field, Romanian otherwise;
 *  - a student is in their final year when their study level is the highest one among the
 *    study lines of their field.
 */
class GetAcademicCalendarUseCase(
    private val academicCalendarRepository: AcademicCalendarRepository,
    private val studyLinesRepository: StudyLinesRepository,
    private val timetablePreferences: TimetablePreferences,
) {

    operator fun invoke(): Flow<AcademicCalendar?> {
        return combine(
            academicCalendarRepository.getCalendar().filterNot { it.status.isLoading() },
            studyLinesRepository.getStudyLines().filterNot { it.status.isLoading() },
            timetablePreferences.getConfiguration(),
        ) { calendarResource, studyLinesResource, configuration ->
            val academicCalendar = calendarResource.payload ?: return@combine null
            val field = configuration?.fieldId?.let(Field::getById)
            val studyLanguage = field?.studyLanguages?.firstOrNull() ?: StudyLanguage.ROMANIAN
            val finalStudyLevel = studyLinesResource.payload
                ?.filter { it.field == field }
                ?.maxOfOrNull { it.level }

            val isFinal = field != null && configuration.studyLevelId == finalStudyLevel?.id
            val semesters = academicCalendar.semesters
                .filter { studyLanguage in it.studyLanguages }
                .groupBy { it.semester }
                .map { (_, variants) ->
                    variants.firstOrNull { it.isFinal == isFinal } ?: variants.first()
                }

            academicCalendar.copy(semesters = semesters)
        }
    }
}
