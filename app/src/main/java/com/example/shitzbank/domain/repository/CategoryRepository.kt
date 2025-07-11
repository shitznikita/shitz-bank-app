package com.example.shitzbank.domain.repository

import com.example.shitzbank.domain.model.Category

/**
 * Интерфейс репозитория для управления данными категорий ([Category]).
 * Определяет операцию по получению информации о доступных категориях.
 *
 * Абстрагирует доменный слой от деталей реализации хранения и получения данных категорий.
 */
interface CategoryRepository {
    /**
     * Получает список всех доступных категорий.
     *
     * @return Список объектов [Category], представляющих все доступные категории.
     * Может вернуть пустой список, если категории отсутствуют.
     * @throws Exception Может выбросить исключение в случае ошибки получения категорий (например, сетевой ошибки).
     */
    suspend fun getCategories(): List<Category>

    suspend fun getCategoriesByType(
        isIncome: Boolean
    ): List<Category>
}
