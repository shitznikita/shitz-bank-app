package com.example.shitzbank.common.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.shitzbank.common.utils.getCurrencySymbol
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

@Composable
fun PriceDisplay(
    amount: Double,
    currency: String,
    modifier: Modifier = Modifier
) {

    val symbols = DecimalFormatSymbols(Locale.getDefault()).apply {
        groupingSeparator = ' '
        decimalSeparator = ','
    }
    val formatter = DecimalFormat("#,##0", symbols)

    val formattedAmount = formatter.format(amount)

    CommonText(
        modifier = modifier,
        text = "$formattedAmount ${getCurrencySymbol(currency)}",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimary
    )
}