package com.example.shitzbank.data.dtos

import com.example.shitzbank.data.local.entity.CategoryEntity
import com.example.shitzbank.domain.model.Category
import kotlinx.serialization.Serializable

@Serializable
data class CategoryDto(
    val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean,
) {
    fun toDomain(): Category {
        return Category(
            id = this.id,
            name = this.name,
            emoji = this.emoji,
            isIncome = this.isIncome,
        )
    }

    fun toEntity(): CategoryEntity {
        return CategoryEntity(
            id = this.id,
            name = this.name,
            emoji = this.emoji,
            isIncome = this.isIncome,
        )
    }
}
