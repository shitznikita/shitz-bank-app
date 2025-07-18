package com.example.shitzbank.ui.common.trail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.currency.Currency
import com.example.shitzbank.common.utils.datetime.formatDateTime
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.PercentageDisplay
import com.example.shitzbank.ui.common.composable.TrailingContent
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun PercentageTrailingContent(
    amount: Double,
    percentage: String,
    currency: String
) {
    val symbols =
        DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = ' '
            decimalSeparator = ','
        }
    val formatter = DecimalFormat("#,##0", symbols)

    val formattedAmount = formatter.format(amount)

    Column {
        TrailingContent(
            content = {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    PercentageDisplay(
                        percentage = percentage,
                    )
                    CommonText(
                        text = "$formattedAmount ${Currency.fromCode(currency)}",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            },
            icon = {
                Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
            },
        )
    }
}