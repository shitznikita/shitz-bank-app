package com.example.shitzbank.ui.common.trail

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.composable.PriceDisplay
import com.example.shitzbank.ui.common.composable.TrailingContent

/**
 * Composable-функция, предназначенная для отображения суммы и валюты
 * с дополнительной иконкой "drill-in" (переход к деталям).
 *
 * Она комбинирует отображение цены ([PriceDisplay]) с иконкой, указывающей на возможность
 * перехода к более детальной информации.
 *
 * @param amount Сумма, которую необходимо отобразить.
 * @param currency Валюта суммы (например, "₽", "$", "€").
 */
@Composable
fun AmountTrailingContent(
    amount: Double,
    currency: String,
) {
    TrailingContent(
        content = {
            PriceDisplay(
                amount = amount,
                currency = currency,
            )
        },
        icon = {
            Icon(ImageVector.vectorResource(R.drawable.drill_in), null)
        },
    )
}
