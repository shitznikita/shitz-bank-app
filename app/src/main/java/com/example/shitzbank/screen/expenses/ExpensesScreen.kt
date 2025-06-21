package com.example.shitzbank.screen.expenses.ui

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
import com.example.shitzbank.common.ui.AmountTrailingContent
import com.example.shitzbank.common.ui.CommonLazyColumn
import com.example.shitzbank.common.ui.CommonListItem
import com.example.shitzbank.common.ui.CommonText
import com.example.shitzbank.common.ui.LeadIcon
import com.example.shitzbank.common.ui.ResultStateHandler
import com.example.shitzbank.common.ui.TotalAmountListItem

@Composable
fun ExpensesScreen(
    viewModel: ExpensesViewModel = hiltViewModel()
) {
    val state by viewModel.expensesState.collectAsState()
    val totalAmount by viewModel.totalExpense.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadExpenses()
    }

    ResultStateHandler(
        state = state,
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
                        lead = {
                            LeadIcon(
                                label = item.category.emoji,
                                backgroundColor = MaterialTheme.colorScheme.secondary
                            )
                        },
                        content = {
                            CommonText(
                                text = item.category.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        },
                        supportingContent = if (item.comment != null) {
                            {
                                CommonText(
                                    text = item.comment,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondary
                                )
                            }
                        } else null,
                        trail = {
                            AmountTrailingContent(item.amount, item.account.currency)
                        }
                    )
                }
            )
        }
    )
}