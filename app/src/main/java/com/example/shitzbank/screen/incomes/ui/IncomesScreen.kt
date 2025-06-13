package com.example.shitzbank.screen.incomes.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.common.AmountTrailingContent
import com.example.shitzbank.ui.common.TotalAmountListItem
import com.example.shitzbank.ui.viewmodel.MainViewModel

@Composable
fun IncomesScreen(viewModel: MainViewModel) {
    val mock by viewModel.incomeTransactions.collectAsState()
    val totalAmount by viewModel.totalIncome.collectAsState()

    ResultStateHandler(
        state = mock,
        onSuccess = { data ->
            CommonLazyColumn(
                topItem = {
                    TotalAmountListItem(totalAmount)
                },
                itemsList = data,
                itemTemplate = { item ->
                    CommonListItem(
                        modifier = Modifier.height(68.dp)
                            .clickable {  },
                        content = { Text(item.title) },
                        trail = {
                            AmountTrailingContent(item.amount, item.currency)
                        }
                    )
                }
            )
        }
    )
}