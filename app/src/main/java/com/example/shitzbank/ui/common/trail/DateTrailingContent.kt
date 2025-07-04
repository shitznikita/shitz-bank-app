package com.example.shitzbank.ui.common.trail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.common.utils.datetime.formatDateTime
import com.example.shitzbank.ui.common.CommonText
import com.example.shitzbank.ui.common.PriceDisplay
import com.example.shitzbank.ui.common.TrailingContent
import java.time.LocalDateTime

/**
 * Composable-функция, предназначенная для отображения суммы, валюты и даты/времени
 * с дополнительной иконкой "drill-in" (переход к деталям).
 *
 * Она комбинирует отображение цены ([PriceDisplay]) и отформатированной даты/времени
 * с иконкой, указывающей на возможность перехода к более детальной информации.
 *
 * @param amount Сумма, которую необходимо отобразить.
 * @param currency Валюта суммы (например, "₽", "$", "€").
 * @param date Объект [LocalDateTime], который будет отформатирован и отображен под суммой.
 */
@Composable
fun DateTrailingContent(
    amount: Double,
    currency: String,
    date: LocalDateTime,
) {
    Column {
        TrailingContent(
            content = {
                Column(
                    horizontalAlignment = Alignment.End,
                ) {
                    PriceDisplay(
                        amount = amount,
                        currency = currency,
                    )
                    CommonText(
                        text = formatDateTime(date),
                        color = MaterialTheme.colorScheme.onSecondary,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            },
            icon = {
                Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
            },
        )
    }
}
