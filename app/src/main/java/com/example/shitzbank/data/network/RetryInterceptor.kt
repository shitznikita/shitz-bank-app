package com.example.shitzbank.data.network

import okhttp3.Interceptor
import okhttp3.Response

class RetryInterceptor : Interceptor {

    private val MAX_RETRIES = 3

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var response: Response? = null
        var tryCount = 0

        while (tryCount < MAX_RETRIES) {
            try {
                response = chain.proceed(request)

                if (response.code < 500) {
                    return response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            ++tryCount
        }

        return response ?: throw IllegalStateException("Failed to get response after multiple retries")
    }

}