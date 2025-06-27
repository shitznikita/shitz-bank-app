package com.example.shitzbank.data.network

import com.example.shitzbank.API_TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Перехватчик OkHttp, который добавляет токен авторизации к каждому исходящему сетевому запросу.
 *
 * Используется для аутентификации запросов к защищенным конечным точкам API.
 * Этот перехватчик должен быть добавлен в [okhttp3.OkHttpClient].
 *
 * @property API_TOKEN Строковая константа, содержащая токен авторизации для API.
 */
@Singleton
class AuthInterceptor
    @Inject
    constructor() : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            val token = API_TOKEN

            val request =
                originalRequest.newBuilder()
                    .header("Authorization", token)
                    .build()

            return chain.proceed(request)
        }
    }
