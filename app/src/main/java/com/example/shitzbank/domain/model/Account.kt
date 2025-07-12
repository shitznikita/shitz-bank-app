package com.example.shitzbank.domain.model

import java.time.LocalDateTime

/**
 * Модель данных, представляющая банковский счет пользователя.
 *
 * @property id Уникальный идентификатор счета.
 * @property userId Идентификатор пользователя, которому принадлежит счет.
 * @property name Название счета (например, "Основной счет", "Сберегательный счет").
 * @property balance Текущий баланс счета.
 * @property currency Валюта счета (например, "USD", "EUR", "RUB").
 * @property createdAt Дата и время создания счета.
 * @property updatedAt Дата и время последнего обновления счета.
 */
data class Account(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
) {
    fun toBrief(): AccountBrief {
        return AccountBrief(
            id = this.id,
            name = this.name,
            balance = this.balance,
            currency = this.currency
        )
    }
}
