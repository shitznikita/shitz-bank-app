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
import com.example.shitzbank.ui.screen.analysis.CategorySummary

@Composable
fun AnalysisItemTemplate(
    item: CategorySummary,
    currency: String,
    total: Double
) {
    val percentage = if (total != 0.0) {
        (item.totalAmount/ total * 100).let {
            "%.1f%%".format(it)
        }
    } else {
        "0%"
    }

    CommonListItem(
        modifier = Modifier.clickable {}
            .height(68.dp),
        lead = {
            LeadIcon(
                label = item.categoryEmoji,
                backgroundColor = MaterialTheme.colorScheme.secondary,
            )
        },
        content = {
            CommonText(
                text = item.categoryName,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        trail = {
            PercentageTrailingContent(
                amount = item.totalAmount,
                percentage = percentage,
                currency = currency
            )
        },
    )
}