package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class StatItemDto(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: Double,
)
