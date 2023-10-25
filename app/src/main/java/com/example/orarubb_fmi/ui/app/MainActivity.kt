package com.example.orarubb_fmi.ui.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.orarubb_fmi.common.timetablesInfo
import com.example.orarubb_fmi.ui.theme.OrarUBBFMITheme
import com.example.orarubb_fmi.ui.viewmodel.TimetableViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ComponentActivity() {

    private val timetableViewModel: TimetableViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            OrarUBBFMITheme {
                OrarUbbFmiApp()
            }
        }

        timetableViewModel.getTimetable(timetablesInfo.first())
        timetableViewModel.getTimetable(timetablesInfo[22])
        timetableViewModel.getGroups(timetablesInfo.first())
    }
}
