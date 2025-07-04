package com.example.shitzbank.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.example.shitzbank.common.utils.currency.Currency
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

/**
 * Composable-функция для отображения денежной суммы с отформатированным числом и символом валюты.
 *
 * Форматирует число с использованием локальных настроек (разделение тысяч пробелом,
 * десятичный разделитель - запятая) и добавляет соответствующий символ валюты.
 *
 * @param amount Денежная сумма типа [Double], которую необходимо отформатировать и отобразить.
 * @param currency Код валюты в виде [String] (например, "RUB", "USD", "EUR"), для которого будет получен символ.
 * @param modifier [Modifier], применяемый к [CommonText] для настройки его внешнего вида и поведения.
 */
@Composable
fun PriceDisplay(
    amount: Double,
    currency: String,
    modifier: Modifier = Modifier,
) {
    val symbols =
        DecimalFormatSymbols(Locale.getDefault()).apply {
            groupingSeparator = ' '
            decimalSeparator = ','
        }
    val formatter = DecimalFormat("#,##0", symbols)

    val formattedAmount = formatter.format(amount)

    CommonText(
        modifier = modifier,
        text = "$formattedAmount ${Currency.fromCode(currency)}",
        textAlign = TextAlign.Start,
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onPrimary,
    )
}
