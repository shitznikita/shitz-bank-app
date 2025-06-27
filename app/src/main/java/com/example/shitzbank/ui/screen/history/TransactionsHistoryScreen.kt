package com.example.shitzbank.ui.screen.history

import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.localdate.toEpochDayMillis
import com.example.shitzbank.common.utils.localdate.toLocalDate
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.CommonText
import com.example.shitzbank.ui.common.CustomDatePicker
import com.example.shitzbank.ui.common.DateListItem
import com.example.shitzbank.ui.common.DateTrailingContent
import com.example.shitzbank.ui.common.LeadIcon
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.common.TotalAmountListItem
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@Composable
fun TransactionsHistoryScreen(viewModel: TransactionsHistoryViewModel = hiltViewModel()) {
    val transactionsState by viewModel.transactionsState.collectAsState()
    val total by viewModel.total.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    ResultStateHandler(
        state = transactionsState,
        onSuccess = { data ->
            CommonLazyColumn(
                topItem = {
                    TransactionsHistoryHeader(
                        startDate = startDate,
                        onStartDateClick = { showStartDatePicker = true },
                        endDate = endDate,
                        onEndDateClick = { showEndDatePicker = true },
                        total = total,
                    )
                },
                itemsList = data,
                itemTemplate = { item ->
                    TransactionsHistoryItemTemplate(item)
                },
            )
        },
    )

    if (showStartDatePicker) {
        StartDatePicker(
            currentStartDate = startDate,
            currentEndDate = endDate,
            onDateSelected = { newDate ->
                viewModel.setStartDate(newDate)
                showStartDatePicker = false
            },
            onDismiss = { showStartDatePicker = false },
        )
    }

    if (showEndDatePicker) {
        EndDatePicker(
            currentStartDate = startDate,
            currentEndDate = endDate,
            onDateSelected = { newDate ->
                viewModel.setEndDate(newDate)
                showEndDatePicker = false
            },
            onDismiss = { showEndDatePicker = false },
        )
    }
}

@Composable
fun TransactionsHistoryHeader(
    startDate: LocalDate,
    onStartDateClick: () -> Unit,
    endDate: LocalDate,
    onEndDateClick: () -> Unit,
    total: Double,
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
    TotalAmountListItem(total)
}

@Composable
fun TransactionsHistoryItemTemplate(item: TransactionResponse) {
    CommonListItem(
        modifier = Modifier.clickable { },
        lead = {
            LeadIcon(
                label = item.category.emoji,
                backgroundColor = MaterialTheme.colorScheme.secondary,
            )
        },
        content = {
            CommonText(
                text = item.category.name,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        supportingContent =
            if (item.comment != null) {
                {
                    CommonText(
                        text = item.comment,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondary,
                    )
                }
            } else {
                null
            },
        trail = {
            DateTrailingContent(
                amount = item.amount,
                currency = item.account.currency,
                date = item.transactionDate,
            )
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StartDatePicker(
    currentStartDate: LocalDate,
    currentEndDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val startDatePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = currentStartDate.toEpochDayMillis(),
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val selectedDate = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                        return !selectedDate.isAfter(currentEndDate)
                    }
                },
        )

    CustomDatePicker(
        datePickerState = startDatePickerState,
        onDateSelected = { newDateMillis ->
            if (newDateMillis != null) {
                onDateSelected(newDateMillis.toLocalDate())
            } else {
                onDateSelected(LocalDate.now().withDayOfMonth(1))
            }
        },
        onDismiss = onDismiss,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EndDatePicker(
    currentStartDate: LocalDate,
    currentEndDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    onDismiss: () -> Unit,
) {
    val endDatePickerState =
        rememberDatePickerState(
            initialSelectedDateMillis = currentEndDate.toEpochDayMillis(),
            selectableDates =
                object : SelectableDates {
                    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                        val selectedDate = Instant.ofEpochMilli(utcTimeMillis).atZone(ZoneId.systemDefault()).toLocalDate()
                        return !selectedDate.isBefore(currentStartDate)
                    }
                },
        )

    CustomDatePicker(
        datePickerState = endDatePickerState,
        onDateSelected = { newDateMillis ->
            if (newDateMillis != null) {
                onDateSelected(newDateMillis.toLocalDate())
            } else {
                onDateSelected(LocalDate.now())
            }
        },
        onDismiss = onDismiss,
    )
}
