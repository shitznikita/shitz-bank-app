package com.example.shitzbank.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.theme.LightGreen

@Composable
fun TotalAmountListItem(
    totalAmount: Double
) {
    CommonListItem(
        backgroundColor = LightGreen,
        content = {
            Text(
                stringResource(R.string.in_total),
                style = MaterialTheme.typography.bodyLarge
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