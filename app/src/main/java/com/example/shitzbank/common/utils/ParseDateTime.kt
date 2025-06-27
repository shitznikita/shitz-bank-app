package com.example.shitzbank.common.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Разбирает строку, представляющую дату и время, в объект [LocalDateTime].
 *
 * Использует стандартный формат ISO 8601 ([DateTimeFormatter.ISO_DATE_TIME]) для разбора строки.
 * Примеры допустимых форматов строк: "2023-01-20T10:30:00", "2023-01-20T10:30:00.123".
 *
 * @param dateTimeString Строка даты и времени в формате ISO 8601.
 * @return Объект [LocalDateTime], соответствующий разобранной строке.
 * @throws java.time.format.DateTimeParseException Если строка не соответствует ожидаемому формату.
 */
fun parseDateTime(dateTimeString: String): LocalDateTime {
    return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
}
