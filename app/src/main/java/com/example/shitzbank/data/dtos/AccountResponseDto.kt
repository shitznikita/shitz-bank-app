package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountResponseDto(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val incomeStats: List<StatItemDto>,
    val expenseStats: List<StatItemDto>,
    val createdAt: String,
    val updatedAt: String,
)
