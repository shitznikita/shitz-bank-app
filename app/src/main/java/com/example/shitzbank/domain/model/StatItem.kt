package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class StatItem(
    val categoryId: Int,
    val categoryName: String,
    val emoji: String,
    val amount: Double
)