package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequestDto(
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
)
