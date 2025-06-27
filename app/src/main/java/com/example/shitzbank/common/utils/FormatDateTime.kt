package com.example.shitzbank.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Форматирует объект [LocalDateTime] в строку в формате "ЧЧ:мм дд.ММ"
 * с использованием региональных настроек по умолчанию.
 *
 * @param date Объект [LocalDateTime], который необходимо отформатировать.
 * @return Отформатированная строка даты и времени (например, "14:30 27.06").
 */
fun formatDateTime(date: LocalDateTime): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm dd.MM", Locale.getDefault())
    return date.format(formatter)
}
