package com.example.orarubb_fmi

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.orarubb_fmi.datasource.api.TimetableApiService
import com.example.orarubb_fmi.domain.ClassType
import com.example.orarubb_fmi.domain.Participant
import com.example.orarubb_fmi.domain.StudyField
import com.example.orarubb_fmi.domain.StudyLanguage
import com.example.orarubb_fmi.domain.Timetable
import com.example.orarubb_fmi.domain.TimetableClass
import com.example.orarubb_fmi.domain.TimetableInfo
import com.example.orarubb_fmi.domain.Week
import com.example.orarubb_fmi.ui.theme.OrarUBBFMITheme
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.koin.android.ext.android.inject

private const val HEADLINE_TAG = "h1"
private const val TABLE_TAG = "table"
private const val TABLE_ROW_TAG = "tr"
private const val TABLE_COLUMN_TAG = "td"
private const val HOURS_DELIMITER = "-"
private const val START_HOUR = 0
private const val END_HOUR = 1
private const val HOUR_FORMAT = "%02d:00"

class MainActivity : ComponentActivity() {

    private val timetableApiService: TimetableApiService by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val timetablesInfo = listOf(
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ROMANIAN,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ENGLISH,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ENGLISH,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ENGLISH,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.MATHEMATICS_COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.HUNGARIAN,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ENGLISH,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ENGLISH,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.ENGLISH,
                studyYear = 3
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.GERMAN,
                studyYear = 1
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.GERMAN,
                studyYear = 2
            ),
            TimetableInfo(
                year = 2022,
                semester = 2,
                studyField = StudyField.COMPUTER_SCIENCE,
                studyLanguage = StudyLanguage.GERMAN,
                studyYear = 3
            )
        )

        lifecycleScope.launch {
            timetablesInfo.forEach { timetableInfo ->
                Log.d("TESTMESSAGE", "$timetableInfo")

                val response = timetableApiService.getTimetablesHtml(
                    year = timetableInfo.year,
                    semester = timetableInfo.semester,
                    studyField = timetableInfo.studyField.notation,
                    studyLanguage = timetableInfo.studyLanguage.notation,
                    studyYear = timetableInfo.studyYear
                )
                val document = Jsoup.parse(response.string())

                val tables = document.select(TABLE_TAG)
                val headlineTags = document.select(HEADLINE_TAG)
                val groupTags = headlineTags.subList(1, headlineTags.size)
                val groups = groupTags.mapNotNull { tag ->
                    tag.text().split(" ").lastOrNull()
                }

                val timetables = tables.mapIndexed { index, table ->
                    val group = groups[index]
                    val rows = table.select(TABLE_ROW_TAG)

                    val timetableClasses = rows.mapNotNull { row ->
                        val columns = row.select(TABLE_COLUMN_TAG)
                        if (columns.isNotEmpty()) {
                            val dayElement = columns[0].text()
                            val hourIntervalElement = columns[1].text()
                            val weekElement = columns[2].text()
                            val placeElement = columns[3].text()
                            val participantElement = columns[4].text()
                            val classTypeElement = columns[5].text()
                            val disciplineElement = columns[6].text()
                            val professorElement = columns[7].text()

                            val hours = hourIntervalElement.split(HOURS_DELIMITER)
                            val startHour = String.format(HOUR_FORMAT, hours[START_HOUR].toInt())
                            val endHour = String.format(HOUR_FORMAT, hours[END_HOUR].toInt())

                            val week = Week.getWeekType(weekElement)
                            val participant = Participant.getParticipantType(participantElement)
                            val classType = ClassType.getClassType(classTypeElement)

                            TimetableClass(
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
                        } else {
                            null
                        }
                    }

                    Timetable(
                        group = group,
                        info = timetableInfo,
                        classes = timetableClasses
                    )
                }
            }
        }

        setContent {
            OrarUBBFMITheme {
                OrarUbbFmiApp()
            }
        }
    }
}
