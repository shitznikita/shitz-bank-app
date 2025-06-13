package com.example.shitzbank.ui.common

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R

@Composable
fun AmountTrailingContent(
    amount: Double,
    currency: String
) {
    TrailingContent(
        content = {
            PriceDisplay(
                amount = amount,
                currencySymbol = currency
            )
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
        }
    )
}