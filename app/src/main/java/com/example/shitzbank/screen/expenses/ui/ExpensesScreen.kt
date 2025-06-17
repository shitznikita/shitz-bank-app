package com.example.shitzbank.screen.expenses.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shitzbank.ui.common.AmountTrailingContent
import com.example.shitzbank.ui.common.CommonLazyColumn
import com.example.shitzbank.ui.common.CommonListItem
import com.example.shitzbank.ui.common.CommonText
import com.example.shitzbank.ui.common.LeadIcon
import com.example.shitzbank.ui.common.ResultStateHandler
import com.example.shitzbank.ui.common.TotalAmountListItem
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
                        lead = { item.icon?.let { LeadIcon(label = it, backgroundColor = MaterialTheme.colorScheme.secondary) } },
                        content = {
                                CommonText(
                                    text = item.title,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                        },
                        supportingContent = if (item.subtitle != null) {
                            {
                                CommonText(
                                    text = item.subtitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondary
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