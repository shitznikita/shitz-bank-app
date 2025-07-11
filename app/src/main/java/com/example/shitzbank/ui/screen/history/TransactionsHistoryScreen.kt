package com.example.shitzbank.ui.screen.history

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.shitzbank.ui.common.composable.CommonLazyColumn
import com.example.shitzbank.ui.common.composable.ResultStateHandler
import com.example.shitzbank.ui.screen.history.common.EndDatePicker
import com.example.shitzbank.ui.screen.history.common.StartDatePicker
import com.example.shitzbank.ui.screen.history.common.TransactionsHistoryHeader
import com.example.shitzbank.ui.screen.history.common.TransactionsHistoryItemTemplate

@Composable
fun TransactionsHistoryScreen(
    navController: NavController,
    viewModel: TransactionsHistoryViewModel = hiltViewModel()
) {
    val transactionsState by viewModel.transactionsState.collectAsState()
    val total by viewModel.total.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val currency by viewModel.accountCurrency.collectAsState()

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
                        currency = currency
                    )
                },
                itemsList = data,
                itemTemplate = { item ->
                    TransactionsHistoryItemTemplate(
                        item = item,
                        onItemClick = {
                            val isIncome = it.category.isIncome
                            val id = it.id
                            navController.navigate("transaction/$isIncome/$id")
                        }
                    )
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
