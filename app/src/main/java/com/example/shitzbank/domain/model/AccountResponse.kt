package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponse(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val incomeStats: List<StatItem>,
    val expenseStats: List<StatItem>,
    val createdAt: String,
    val updatedAt: String
)