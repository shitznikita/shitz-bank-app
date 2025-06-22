package com.example.shitzbank.common.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.formatDateTime

@Composable
fun DateTrailingContent(
    amount: Double,
    currency: String,
    date: String
) {
    Column {
        TrailingContent(
            content = {
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    PriceDisplay(
                        amount = amount,
                        currency = currency
                    )
                    CommonText(
                        text = formatDateTime(date),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            },
            icon = {
                Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
            }
        )
    }
}