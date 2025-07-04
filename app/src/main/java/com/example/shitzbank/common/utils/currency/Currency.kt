package com.example.shitzbank.common.utils.currency

import com.example.shitzbank.R

enum class Currency(
    val code: String,
    val fullName: String,
    val symbol: String,
    val iconResId: Int
) {
    RUB("RUB", "Российский рубль", "₽", R.drawable.ic_ruble),
    EUR("EUR", "Евро", "€", R.drawable.ic_euro),
    USD("USD", "Американский доллар", "$", R.drawable.ic_dollar);

    companion object {

        fun fromCode(code: String): String = entries.find { it.code == code }?.symbol ?: ""

        fun getAllCurrencies(): List<Currency> = entries

    }
}
