package com.example.shitzbank.domain.model

/**
 * Упрощенная модель данных, представляющая краткую информацию о банковском счете.
 * Используется для отображения основных сведений о счете без полной детализации.
 *
 * @property id Уникальный идентификатор счета.
 * @property name Название счета (например, "Основной счет", "Сберегательный счет").
 * @property balance Текущий баланс счета.
 * @property currency Валюта счета (например, "USD", "EUR", "RUB").
 */
data class AccountBrief(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
)
