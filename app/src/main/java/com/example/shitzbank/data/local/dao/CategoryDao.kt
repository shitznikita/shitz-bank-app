package com.example.shitzbank.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shitzbank.data.dtos.CategoryDto
import com.example.shitzbank.data.local.entity.CategoryEntity
import com.example.shitzbank.domain.model.Category

@Dao
interface CategoryDao {
    /**
     * Вставляет список категорий в базу данных.
     * Принимает список DTO, маппит их в Entity и вставляет.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryDto>) {
        insertCategoryEntities(categories.map { it.toEntity() })
    }

    /**
     * Вспомогательный приватный метод для Room, принимает List<CategoryEntity>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryEntities(categories: List<CategoryEntity>)

    /**
     * Получает все категории из базы данных.
     * Возвращает список доменных моделей Category.
     */
    @Query("SELECT * FROM categories")
    suspend fun getCategories(): List<Category> {
        return getCategoryEntities().map { it.toDomain() }
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает List<CategoryEntity>
     */
    @Query("SELECT * FROM categories")
    suspend fun getCategoryEntities(): List<CategoryEntity>

    /**
     * Получает категории по их типу (доход/расход).
     * Возвращает список доменных моделей Category.
     */
    @Query("SELECT * FROM categories WHERE isIncome = :isIncome")
    suspend fun getCategoriesByType(isIncome: Boolean): List<Category> {
        return getCategoryEntitiesByType(isIncome).map { it.toDomain() }
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает List<CategoryEntity>
     */
    @Query("SELECT * FROM categories WHERE isIncome = :isIncome")
    suspend fun getCategoryEntitiesByType(isIncome: Boolean): List<CategoryEntity>

    /**
     * Получает категорию по ее ID.
     * Возвращает доменную модель Category или null.
     */
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryById(categoryId: Int): Category? {
        return getCategoryEntityById(categoryId)?.toDomain()
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает CategoryEntity
     */
    @Query("SELECT * FROM categories WHERE id = :categoryId")
    suspend fun getCategoryEntityById(categoryId: Int): CategoryEntity?

    /**
     * Удаляет все категории из базы данных.
     */
    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories()
}