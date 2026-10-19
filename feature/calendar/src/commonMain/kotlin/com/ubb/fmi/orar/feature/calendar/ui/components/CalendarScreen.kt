package com.ubb.fmi.orar.feature.calendar.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ubb.fmi.orar.feature.calendar.ui.viewmodel.model.CalendarUiState
import com.ubb.fmi.orar.ui.catalog.components.TopBar
import com.ubb.fmi.orar.ui.catalog.components.state.StateScaffold
import com.ubb.fmi.orar.ui.theme.OrarUbbFmiTheme
import com.ubb.fmi.orar.ui.theme.Pds
import kotlinx.coroutines.launch
import orar_ubb_fmi.ui.catalog.generated.resources.Res
import orar_ubb_fmi.ui.catalog.generated.resources.lbl_calendar
import org.jetbrains.compose.resources.stringResource

/**
 * Displays the academic year one month per page, with every day colored by its academic activity.
 */
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onRetryClick: () -> Unit,
    onBack: () -> Unit,
) {
    StateScaffold(
        isLoading = uiState.isLoading,
        isEmpty = uiState.months.isEmpty(),
        errorStatus = uiState.errorStatus,
        onRetryClick = onRetryClick,
        topBar = {
            TopBar(
                title = stringResource(Res.string.lbl_calendar),
                onBack = onBack
            )
        }
    ) { paddingValues ->
        val coroutineScope = rememberCoroutineScope()
        val pagerState = rememberPagerState(initialPage = uiState.initialPage) { uiState.months.size }
        val currentMonth = uiState.months[pagerState.currentPage]

        Column(
            verticalArrangement = Arrangement.spacedBy(Pds.spacing.Medium),
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(vertical = Pds.spacing.Medium)
        ) {
            MonthHeader(
                modifier = Modifier.padding(horizontal = Pds.spacing.Medium),
                monthDate = currentMonth.firstDate,
                isPreviousEnabled = pagerState.currentPage > 0,
                isNextEnabled = pagerState.currentPage < uiState.months.lastIndex,
                onPreviousClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage.dec())
                    }
                },
                onNextClick = {
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage.inc())
                    }
                },
            )

            HorizontalPager(
                state = pagerState,
                verticalAlignment = Alignment.Top,
                pageSpacing = Pds.spacing.Medium,
                contentPadding = PaddingValues(horizontal = Pds.spacing.Medium)
            ) { page ->
                ElevatedCard {
                    MonthGrid(
                        month = uiState.months[page],
                        modifier = Modifier.padding(Pds.spacing.SMedium)
                    )
                }
            }

            CalendarLegend(
                modifier = Modifier.padding(horizontal = Pds.spacing.Medium),
            )

            CalendarPeriods(
                periods = uiState.periods,
                modifier = Modifier.padding(horizontal = Pds.spacing.Medium),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewCalendarScreen() {
    OrarUbbFmiTheme {
        CalendarScreen(
            uiState = CalendarUiState(isLoading = true),
            onRetryClick = {},
            onBack = {},
        )
    }
}
