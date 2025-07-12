package com.example.shitzbank.ui.screen.incomes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.shitzbank.ui.common.trail.AmountTrailingContent
import com.example.shitzbank.ui.common.composable.CommonLazyColumn
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.ResultStateHandler
import com.example.shitzbank.ui.common.list.TotalAmountListItem

@Composable
fun IncomesScreen(
    navController: NavController,
    viewModel: IncomesViewModel = hiltViewModel()
) {
    val state by viewModel.incomesState.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()
    val currency by viewModel.accountCurrency.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadIncomes()
    }

    val navBackStackEntry = navController.currentBackStackEntry

    LaunchedEffect(navBackStackEntry) {
        val refreshNeeded = navBackStackEntry?.savedStateHandle?.get<Boolean>("refresh_needed")
        if (refreshNeeded == true) {
            viewModel.loadIncomes()
            navBackStackEntry.savedStateHandle["refresh_needed"] = false
        }
    }

    ResultStateHandler(
        state = state,
        onSuccess = { data ->
            CommonLazyColumn(
                topItem = {
                    TotalAmountListItem(
                        totalAmount = totalIncome,
                        currency = currency
                    )
                },
                itemsList = data,
                itemTemplate = { item ->
                    CommonListItem(
                        modifier =
                            Modifier.height(68.dp)
                                .clickable {
                                    val isIncome = item.category.isIncome
                                    val id = item.id
                                    navController.navigate("transaction/$isIncome/$id")
                                },
                        content = {
                            CommonText(
                                text = item.category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                            )
                        },
                        trail = {
                            AmountTrailingContent(item.amount, item.account.currency)
                        },
                    )
                },
            )
        },
    )
}
