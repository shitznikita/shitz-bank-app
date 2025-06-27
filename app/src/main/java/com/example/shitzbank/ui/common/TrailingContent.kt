package com.example.shitzbank.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * Composable-функция, предназначенная для выстраивания контента справа от основного элемента,
 * часто используемая как `trailingContent` в элементах списка.
 *
 * Она располагает переданное содержимое (`content`) и опциональную иконку (`icon`)
 * в горизонтальном ряду, центрируя их по вертикали и добавляя отступ между ними.
 *
 * @param content Composable-функция, представляющая основное содержимое, которое будет
 * отображено слева в этом блоке (например, отформатированная сумма).
 * @param icon Опциональная Composable-функция, представляющая иконку, которая будет
 * отображена справа от основного содержимого. Если `null`, иконка не отображается.
 */
@Composable
fun TrailingContent(
    content: @Composable () -> Unit,
    icon: (@Composable () -> Unit)? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.Center) {
            content()
        }
        Spacer(Modifier.width(16.dp))
        icon?.let {
            Box(
                contentAlignment = Alignment.Center,
            ) { it() }
        }
    }
}
