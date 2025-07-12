package com.example.shitzbank.ui.common.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.shitzbank.R
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import com.example.shitzbank.ui.common.composable.PriceDisplay

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
fun TotalAmountListItem(
    totalAmount: Double,
    currency: String
) {
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
                currency = currency,
            )
        },
    )
}
