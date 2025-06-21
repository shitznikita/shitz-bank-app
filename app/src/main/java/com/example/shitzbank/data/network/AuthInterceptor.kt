package com.example.shitzbank.data.network

import com.example.shitzbank.API_TOKEN
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthInterceptor @Inject constructor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val token = API_TOKEN

        val request = originalRequest.newBuilder()
            .header("Authorization", token)
            .build()

        return chain.proceed(request)
    }

}