package com.example.shitzbank.domain.model

import java.time.LocalDateTime

/**
 * Модель данных, представляющая подробную информацию о финансовой транзакции,
 * включая связанные данные счета и категории.
 *
 * Эта модель используется для отображения транзакций с обогащенной информацией,
 * такой как краткие сведения о счете ([AccountBrief]) и полные данные категории ([Category]).
 *
 * @property id Уникальный идентификатор транзакции.
 * @property account Краткая информация о счете ([AccountBrief]), к которому относится транзакция.
 * @property category Информация о категории ([Category]), к которой относится транзакция.
 * @property amount Сумма транзакции. Положительное значение для дохода, отрицательное для расхода.
 * @property transactionDate Дата и время совершения транзакции.
 * @property comment Опциональный комментарий или описание транзакции. Может быть `null`.
 * @property createdAt Дата и время создания записи о транзакции.
 * @property updatedAt Дата и время последнего обновления записи о транзакции.
 */
data class TransactionResponse(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val transactionDate: LocalDateTime,
    val comment: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
)
