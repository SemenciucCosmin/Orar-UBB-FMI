package com.ubb.fmi.orar.data.timetable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ubb.fmi.orar.data.timetable.api.TimetableApi
import com.ubb.fmi.orar.logging.Logger
import kotlinx.coroutines.launch

class TestViewModel(
    private val logger: Logger,
    private val timetableApi: TimetableApi
): ViewModel() {

    fun test() {
        viewModelScope.launch {
            val result = timetableApi.getTimetable()
            logger.log("TESTMESSAGE", "$result")
        }
    }
}