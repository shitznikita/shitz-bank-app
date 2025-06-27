package com.example.shitzbank.domain.model

/**
 * Модель запроса для создания нового банковского счета.
 * Содержит минимально необходимую информацию для инициализации нового счета.
 *
 * @property name Название нового счета (например, "Основной счет", "Сберегательный счет").
 * @property balance Начальный баланс для нового счета.
 * @property currency Валюта нового счета (например, "USD", "EUR", "RUB").
 */
data class AccountCreateRequest(
    val name: String,
    val balance: Double,
    val currency: String,
)
