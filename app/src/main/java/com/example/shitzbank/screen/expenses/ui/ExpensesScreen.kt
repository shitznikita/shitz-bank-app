package com.example.shitzbank.screen.expenses.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.AmountTrailingContent
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.LeadIcon
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.common.TotalAmountListItem
import com.example.shitzbank.ui.theme.LightGreen
import com.example.shitzbank.ui.viewmodel.MainViewModel

@Composable
fun ExpensesScreen(viewModel: MainViewModel) {
    val mock by viewModel.expenseTransactions.collectAsState()
    val totalAmount by viewModel.totalExpense.collectAsState()

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
                        modifier = Modifier
                            .height(68.dp)
                            .clickable { },
                        lead = { item.icon?.let { LeadIcon(label = it, backgroundColor = LightGreen) } },
                        content = {
                                Text(
                                    item.title,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                        },
                        supportingContent = if (item.subtitle != null) {
                            {
                                Text(
                                    item.subtitle,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        } else null,
                        trail = {
                            AmountTrailingContent(item.amount, item.currency)
                        }
                    )
                }
            )
        }
    )
}