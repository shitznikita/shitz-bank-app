package com.example.shitzbank.ui.common.composable

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/**
 * Универсальная Composable-функция для отображения текста с общими настройками стиля и поведения.
 *
 * Эта обертка над [Text] предоставляет удобный способ
 * применять часто используемые параметры текста, такие как цвет, стиль, максимальное количество строк
 * и обработка переполнения.
 *
 * @param modifier [Modifier], применяемый к [Text] для настройки его внешнего вида и поведения.
 * @param text Строка текста для отображения.
 * @param color Цвет текста.
 * @param style Стиль текста ([TextStyle]), определяющий, например, размер шрифта, начертание и семейство.
 * @param maxLines Максимальное количество строк, которые текст может занимать. По умолчанию 1.
 * @param overflow Определяет, как обрабатывать текст, который не помещается в заданные границы.
 * По умолчанию [TextOverflow.MiddleEllipsis] (многоточие в середине).
 * @param textAlign Выравнивание текста в пределах его контейнера.
 * Может быть `null` для использования выравнивания по умолчанию.
 */
@Composable
fun CommonText(
    modifier: Modifier = Modifier,
    text: String,
    color: Color,
    style: TextStyle,
    maxLines: Int = 1,
    overflow: TextOverflow = TextOverflow.MiddleEllipsis,
    textAlign: TextAlign? = null,
) {
    Text(
        text = text,
        color = color,
        style = style,
        maxLines = maxLines,
        overflow = overflow,
        textAlign = textAlign,
        modifier = modifier,
    )
}
