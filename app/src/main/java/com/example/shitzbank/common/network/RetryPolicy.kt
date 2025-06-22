package com.example.shitzbank.common.network

import kotlinx.coroutines.delay
import okio.IOException
import retrofit2.HttpException

suspend fun <T> retryWithBackoff(
    maxRetries: Int = 3,
    initialDelayMillis: Long = 2000L,
    shouldRetryFromError: (Throwable) -> Boolean = { throwable ->
        when (throwable) {
            is IOException -> true
            is HttpException -> throwable.code() >= 500
            else -> false
        }
    },
    block: suspend () -> T
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

    throw IllegalStateException("Failed to execute block after $maxRetries attempts. This should not be reached.")
}