package com.example.shitzbank.common.function

fun getCurrencySymbol(currency: String): String {
    return when (currency.uppercase()) {
        "RUB" -> "₽"
        "USD" -> "$"
        "EUR" -> "€"
        else -> currency
    }
}