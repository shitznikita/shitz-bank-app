package com.example.shitzbank.ui.screen.analysis.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.list.ChipDateListItem
import com.example.shitzbank.ui.common.list.TotalAmountListItem
import java.time.LocalDate

@Composable
fun AnalysisHeader(
    startDate: LocalDate,
    onStartDateClick: () -> Unit,
    endDate: LocalDate,
    onEndDateClick: () -> Unit,
    total: Double,
    currency: String
) {
    ChipDateListItem(
        modifier = Modifier.clickable(onClick = onStartDateClick),
        onClick = onStartDateClick,
        content = stringResource(R.string.expenses_history_begin_date),
        date = startDate,
        backgroundColor = MaterialTheme.colorScheme.background
    )
    ChipDateListItem(
        modifier = Modifier.clickable(onClick = onEndDateClick),
        onClick = onEndDateClick,
        content = stringResource(R.string.expenses_history_end_date),
        date = endDate,
        backgroundColor = MaterialTheme.colorScheme.background
    )
    TotalAmountListItem(
        totalAmount = total,
        currency = currency,
        backgroundColor = MaterialTheme.colorScheme.background
    )
}