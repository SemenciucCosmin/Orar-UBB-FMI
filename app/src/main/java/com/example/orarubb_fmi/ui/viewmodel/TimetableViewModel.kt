package com.example.orarubb_fmi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.orarubb_fmi.domain.repository.TimetableRepository
import com.example.orarubb_fmi.model.Resource
import com.example.orarubb_fmi.model.TimetableInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TimetableViewModel(private val timetableRepository: TimetableRepository) : ViewModel() {

    private val _timetableUiState = MutableStateFlow<TimetableUitState>(TimetableUitState.Loading)
    val timetableUiState = _timetableUiState.asStateFlow()

    fun getTimetable(timetableInfo: TimetableInfo) {
        _timetableUiState.update { TimetableUitState.Loading }
        viewModelScope.launch {
            when (val resource = timetableRepository.getTimetable(timetableInfo)) {
                is Resource.Success -> {
                    _timetableUiState.update {
                        TimetableUitState.Success(
                            groups = resource.data.classes.map { it.group }.distinct(),
                            timetable = resource.data,
                        )
                    }
                }

                is Resource.Error -> {
                    _timetableUiState.update {
                        TimetableUitState.Error(resource.type.toString())
                    }
                }
            }
        }
    }
}
