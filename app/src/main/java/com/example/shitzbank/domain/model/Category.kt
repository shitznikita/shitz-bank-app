package com.example.shitzbank.domain.model

/**
 * Модель данных, представляющая категорию транзакций.
 * Используется для классификации доходов или расходов.
 *
 * @property id Уникальный идентификатор категории.
 * @property name Название категории (например, "Еда", "Транспорт", "Зарплата").
 * @property emoji Символ эмодзи, представляющий категорию для визуального отображения (например, "🍔", "🚌", "💰").
 * @property isIncome Флаг, указывающий, является ли категория доходом (`true`) или расходом (`false`).
 */
data class Category(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
)
