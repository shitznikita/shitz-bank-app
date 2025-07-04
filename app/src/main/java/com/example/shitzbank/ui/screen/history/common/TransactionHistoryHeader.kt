package com.example.shitzbank.ui.screen.history.common

import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.list.DateListItem
import com.example.shitzbank.ui.common.list.TotalAmountListItem
import java.time.LocalDate

@Composable
fun TransactionsHistoryHeader(
    startDate: LocalDate,
    onStartDateClick: () -> Unit,
    endDate: LocalDate,
    onEndDateClick: () -> Unit,
    total: Double,
    currency: String
) {
    DateListItem(
        modifier = Modifier.clickable(onClick = onStartDateClick),
        content = stringResource(R.string.expenses_history_begin_date),
        date = startDate,
    )
    DateListItem(
        modifier = Modifier.clickable(onClick = onEndDateClick),
        content = stringResource(R.string.expenses_history_end_date),
        date = endDate,
    )
    TotalAmountListItem(
        totalAmount = total,
        currency = currency
    )
}