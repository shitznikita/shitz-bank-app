package com.example.shitzbank.ui.common.list

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.shitzbank.common.utils.datetime.formatDate
import com.example.shitzbank.ui.common.composable.CommonListItem
import com.example.shitzbank.ui.common.composable.CommonText
import java.time.LocalDate

/**
 * Composable-функция для отображения элемента списка, содержащего текстовый контент и дату.
 *
 * Этот компонент представляет собой [CommonListItem] с предопределенным стилем,
 * где основной контент отображается слева, а отформатированная дата — справа.
 * Фон элемента устанавливается в [MaterialTheme.colorScheme.secondary].
 *
 * @param modifier [Modifier], применяемый к корневому [CommonListItem] для настройки его внешнего вида и поведения.
 * @param content Основная текстовая строка, отображаемая в левой части элемента списка.
 * @param date Объект [LocalDate], который будет отформатирован и отображен в правой части элемента списка.
 */
@Composable
fun DateListItem(
    modifier: Modifier,
    content: String,
    date: LocalDate,
) {
    CommonListItem(
        modifier = modifier,
        backgroundColor = MaterialTheme.colorScheme.secondary,
        content = {
            CommonText(
                text = content,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
        trail = {
            CommonText(
                text = formatDate(date),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        },
    )
}
