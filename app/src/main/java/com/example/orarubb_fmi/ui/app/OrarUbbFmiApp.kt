package com.example.orarubb_fmi.ui.app

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.orarubb_fmi.common.timetablesInfo
import com.example.orarubb_fmi.ui.viewmodel.TimetableUitState
import com.example.orarubb_fmi.ui.viewmodel.TimetableViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrarUbbFmiApp() {
    val timetableViewModel = koinViewModel<TimetableViewModel>()
    val timetableUiState by timetableViewModel.timetableUiState.collectAsStateWithLifecycle()
    var selectedGroupIndex by remember { mutableIntStateOf(0) }
    var selectedTimetableIndex by remember { mutableIntStateOf(0) }

    Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                    itemsIndexed(timetablesInfo) { index, info ->
                        FilterChip(
                            selected = selectedTimetableIndex == index,
                            onClick = {
                                selectedTimetableIndex = index
                                selectedGroupIndex = 0
                                timetableViewModel.getTimetable(info)
                            },
                            label = {
                                Text(
                                    text = "${info.year}/${info.semester}-" +
                                            "${info.studyField.notation}" +
                                            "${info.studyLanguage.notation}" +
                                            "${info.studyYear}"
                                )
                            }
                        )
                    }
                }
                when (timetableUiState) {
                    is TimetableUitState.Success -> {
                        val timetable = (timetableUiState as TimetableUitState.Success).timetable
                        val groups = (timetableUiState as TimetableUitState.Success).groups

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                            itemsIndexed(groups) { index, group ->
                                FilterChip(
                                    selected = selectedGroupIndex == index,
                                    onClick = { selectedGroupIndex = index },
                                    label = { Text(text = group) }
                                )
                            }
                        }
                        val classes = timetable.classes.filter {
                            it.group == groups[selectedGroupIndex]
                        }

                        LazyColumn(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            items(classes) {
                                Text(text = it.toString())
                            }
                        }
                    }


                    is TimetableUitState.Loading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.size(50.dp),
                            color = Color.Blue
                        )
                    }

                    is TimetableUitState.Error -> {
                        Text(text = (timetableUiState as TimetableUitState.Error).message)
                    }
                }
            }
        }
    }
}
