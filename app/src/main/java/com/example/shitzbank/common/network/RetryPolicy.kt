package com.example.shitzbank.common.network

import kotlinx.coroutines.delay
import okio.IOException
import retrofit2.HttpException

/**
 * Код HTTP-ошибки сервера, начиная с которого следует выполнять повторные попытки.
 * Обычно 500 означает внутреннюю ошибку сервера.
 */
private const val SERVER_ERROR_CODE = 500

/**
 * Выполняет заданный блок кода, применяя механизм повторных попыток с экспоненциальной задержкой
 * в случае возникновения определенных исключений.
 *
 * Эта функция полезна для обработки временных сетевых проблем или серверных ошибок,
 * которые могут быть устранены повторным запросом.
 *
 * @param T Тип возвращаемого значения блока кода.
 * @param maxRetries Максимальное количество попыток выполнения блока. По умолчанию 3.
 * @param initialDelayMillis Начальная задержка между попытками в миллисекундах. По умолчанию 2000 мс.
 * @param shouldRetryFromError Лямбда-функция, которая определяет,
 * следует ли повторять попытку при возникновении данного [Throwable].
 * По умолчанию повторяет для [IOException] и [HttpException]
 * с кодом >= [SERVER_ERROR_CODE].
 * @param block Асинхронный блок кода, который должен быть выполнен.
 * @return Результат выполнения [block].
 * @throws IllegalStateException Если блок не удалось успешно выполнить после [maxRetries] попыток.
 * @throws Throwable Исходное исключение, если оно не соответствует условиям для повторной попытки
 * или если превышено максимальное количество попыток.
 */
suspend fun <T> retryWithBackoff(
    maxRetries: Int = 3,
    initialDelayMillis: Long = 2000L,
    shouldRetryFromError: (Throwable) -> Boolean = { throwable ->
        when (throwable) {
            is IOException -> true
            is HttpException -> throwable.code() >= SERVER_ERROR_CODE
            else -> false
        }
    },
    block: suspend () -> T,
): T {
    var attempts = 0

    while (attempts < maxRetries) {
        try {
            return block()
        } catch (e: Throwable) {
            attempts++
            if (shouldRetryFromError(e) && attempts < maxRetries) {
                delay(initialDelayMillis)
            } else {
                throw e
            }
        }
    }

    // Эта строка должна быть недостижима, если логика корректна и maxRetries > 0.
    // Она служит как "защита от дурака" на случай непредвиденных сценариев.
    throw IllegalStateException("Failed to execute block after $maxRetries attempts. This should not be reached.")
}
