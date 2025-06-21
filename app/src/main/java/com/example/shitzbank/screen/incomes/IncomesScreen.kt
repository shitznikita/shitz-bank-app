package com.example.shitzbank.screen.incomes

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
import com.example.shitzbank.common.ui.CommonLazyColumn
import com.example.shitzbank.common.ui.CommonListItem
import com.example.shitzbank.common.ui.ResultStateHandler
import com.example.shitzbank.common.ui.AmountTrailingContent
import com.example.shitzbank.common.ui.CommonText
import com.example.shitzbank.common.ui.TotalAmountListItem

@Composable
fun IncomesScreen(
    viewModel: IncomesViewModel = hiltViewModel()
) {
    val state by viewModel.incomesState.collectAsState()
    val totalIncome by viewModel.totalIncome.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadIncomes()
    }

    ResultStateHandler(
        state = state,
        onSuccess = { data ->
            CommonLazyColumn(
                topItem = {
                    TotalAmountListItem(totalIncome)
                },
                itemsList = data,
                itemTemplate = { item ->
                    CommonListItem(
                        modifier = Modifier.height(68.dp)
                            .clickable {  },
                        content = {
                            CommonText(
                                text = item.category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        trail = {
                            AmountTrailingContent(item.amount, item.account.currency)
                        }
                    )
                }
            )
        }
    )
}