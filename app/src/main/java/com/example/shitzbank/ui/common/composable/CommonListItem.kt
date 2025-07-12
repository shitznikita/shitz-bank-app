package com.example.shitzbank.ui.common.composable

import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Универсальный Composable-компонент для отображения элемента списка в стиле Material Design.
 *
 * Этот компонент является оберткой над [ListItem] и автоматически добавляет [HorizontalDivider]
 * под каждым элементом для визуального разделения. Он предоставляет гибкие слоты для
 * размещения ведущего контента (иконка/аватар), основного контента (заголовок),
 * дополнительного контента (подзаголовок) и завершающего контента (дополнительная информация/кнопка).
 *
 * @param modifier [Modifier], применяемый к корневому [ListItem] для настройки его внешнего вида и поведения.
 * @param lead Опциональная Composable-функция для отображения ведущего контента (например, иконки или аватара).
 * @param content Composable-функция для отображения основного контента элемента списка (заголовка).
 * @param supportingContent Опциональная Composable-функция для отображения дополнительного,
 * поддерживающего контента (подзаголовка).
 * @param trail Опциональная Composable-функция для отображения завершающего контента (например, стрелки, суммы, даты).
 * @param backgroundColor Цвет фона элемента списка. По умолчанию используется [MaterialTheme.colorScheme.background].
 */
@Composable
fun CommonListItem(
    modifier: Modifier = Modifier,
    lead: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit,
    supportingContent: (@Composable () -> Unit)? = null,
    trail: (@Composable () -> Unit)? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.background,
) {
    ListItem(
        modifier = modifier,
        leadingContent = lead,
        headlineContent = content,
        supportingContent = supportingContent,
        trailingContent = trail,
        colors = ListItemDefaults.colors(containerColor = backgroundColor),
    )
    HorizontalDivider()
}
