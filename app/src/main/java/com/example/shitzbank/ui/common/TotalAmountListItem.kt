package com.example.shitzbank.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R

@Composable
fun TotalAmountListItem(
    totalAmount: Double
) {
    CommonListItem(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        content = {
            CommonText(
                text = stringResource(R.string.in_total),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        },
        trail = {
            PriceDisplay(
                amount = totalAmount,
                currencySymbol = "₽"
            )
        }
    )
}