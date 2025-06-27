package com.example.shitzbank.domain.model

import java.time.LocalDateTime

/**
 * Модель данных, представляющая финансовую транзакцию.
 *
 * @property id Уникальный идентификатор транзакции.
 * @property accountId Идентификатор счета, к которому относится транзакция.
 * @property categoryId Идентификатор категории, к которой относится транзакция (например, "Еда", "Транспорт").
 * @property amount Сумма транзакции. Положительное значение для дохода, отрицательное для расхода.
 * @property transactionDate Дата и время совершения транзакции.
 * @property comment Опциональный комментарий или описание транзакции. Может быть `null`.
 * @property createdAt Дата и время создания записи о транзакции.
 * @property updatedAt Дата и время последнего обновления записи о транзакции.
 */
data class Transaction(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: LocalDateTime,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
