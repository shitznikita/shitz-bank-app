package com.example.shitzbank.ui.screen.history.common

import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.LeadIcon
import com.example.shitzbank.ui.common.trail.DateTrailingContent

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