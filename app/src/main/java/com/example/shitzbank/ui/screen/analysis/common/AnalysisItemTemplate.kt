package com.example.shitzbank.ui.screen.analysis.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.LeadIcon
import com.example.shitzbank.ui.common.trail.PercentageTrailingContent

@Composable
fun AnalysisItemTemplate(
    item: TransactionResponse,
    total: Double,
    onItemClick: (TransactionResponse) -> Unit
) {
    val percentage = if (total != 0.0) {
        (item.amount / total * 100).let {
            "%.1f%%".format(it)
        }
    } else {
        "0%"
    }

    CommonListItem(
        modifier = Modifier.clickable {
            onItemClick(item)
        }
            .height(68.dp),
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
            PercentageTrailingContent(
                amount = item.amount,
                percentage = percentage,
                currency = item.account.currency
            )
        },
    )
}