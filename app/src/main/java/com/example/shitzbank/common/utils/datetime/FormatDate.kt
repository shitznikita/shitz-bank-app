package com.example.shitzbank.common.utils.datetime

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

/**
 * Форматирует объект [LocalDate] в строку в формате "дд.ММ.гггг"
 * с использованием региональных настроек по умолчанию.
 *
 * @param date Объект [LocalDate], который необходимо отформатировать.
 * @return Отформатированная строка даты (например, "27.06.2025").
 */
fun formatDate(date: LocalDate): String {
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
    return formatter.format(date)
}
