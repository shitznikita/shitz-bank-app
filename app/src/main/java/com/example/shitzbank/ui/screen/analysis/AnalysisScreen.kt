package com.example.shitzbank.ui.screen.analysis

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.shitzbank.ui.common.composable.CommonLazyColumn
import com.example.shitzbank.ui.common.composable.ResultStateHandler
import com.example.shitzbank.ui.common.message.NoTransactionMessage
import com.example.shitzbank.ui.common.picker.EndDatePicker
import com.example.shitzbank.ui.common.picker.StartDatePicker
import com.example.shitzbank.ui.screen.analysis.common.AnalysisHeader
import com.example.shitzbank.ui.screen.analysis.common.AnalysisItemTemplate

@Composable
fun AnalysisScreen(
    navController: NavController,
    viewModel: AnalysisViewModel = hiltViewModel()
) {
    val transactionsState by viewModel.categorySummaries.collectAsState()
    val total by viewModel.total.collectAsState()
    val startDate by viewModel.startDate.collectAsState()
    val endDate by viewModel.endDate.collectAsState()
    val currency by viewModel.accountCurrency.collectAsState()

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val navBackStackEntry = navController.currentBackStackEntry

    LaunchedEffect(navBackStackEntry) {
        val refreshNeeded = navBackStackEntry?.savedStateHandle?.get<Boolean>("refresh_needed")
        if (refreshNeeded == true) {
            viewModel.loadTransactions()
            navBackStackEntry.savedStateHandle["refresh_needed"] = false
        }
    }

    ResultStateHandler(
        state = transactionsState,
        onSuccess = { data ->
            if (data.isEmpty()) {
                Column {
                    AnalysisHeader(
                        startDate = startDate,
                        onStartDateClick = { showStartDatePicker = true },
                        endDate = endDate,
                        onEndDateClick = { showEndDatePicker = true },
                        total = total,
                        currency = currency
                    )
                    NoTransactionMessage()
                }
            } else {
                CommonLazyColumn(
                    topItem = {
                        AnalysisHeader(
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
                        AnalysisItemTemplate(
                            item = item,
                            total = total,
                            currency = currency
                        )
                    },
                )
            }
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