package com.example.shitzbank.domain.repository

sealed class ResultState<out T> {
    data class Success<out T>(val data: T) : ResultState<T>()
    object Loading : ResultState<Nothing>()
    data class Error(val message: String? = null, val throwable: Throwable? = null) : ResultState<Nothing>()
}