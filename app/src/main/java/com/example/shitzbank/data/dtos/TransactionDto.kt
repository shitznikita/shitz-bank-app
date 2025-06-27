package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
)
