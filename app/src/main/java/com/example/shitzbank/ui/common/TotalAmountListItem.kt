package com.example.shitzbank.ui.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R

/**
 * Composable-функция, отображающая элемент списка для суммирования итоговой суммы.
 *
 * Этот компонент представляет собой [CommonListItem] с предопределенным стилем,
 * где слева отображается текст "Всего:", а справа — отформатированная итоговая сумма.
 * Фон элемента устанавливается в [MaterialTheme.colorScheme.secondary].
 *
 * @param totalAmount Общая сумма [Double], которую необходимо отобразить.
 */
@Composable
fun TotalAmountListItem(totalAmount: Double) {
    CommonListItem(
        backgroundColor = MaterialTheme.colorScheme.secondary,
        content = {
            CommonText(
                text = stringResource(R.string.in_total),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        trail = {
            PriceDisplay(
                amount = totalAmount,
                currency = "₽",
            )
        },
    )
}
