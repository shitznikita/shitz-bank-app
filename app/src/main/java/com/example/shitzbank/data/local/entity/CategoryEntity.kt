package com.example.shitzbank.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.shitzbank.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val emoji: String,
    val isIncome: Boolean
) {
    fun toDomain(): Category {
        return Category(
            id = this.id,
            name = this.name,
            emoji = this.emoji,
            isIncome = this.isIncome
        )
    }
}
