package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponse(
    val id: Int,
    val account: AccountBrief,
    val category: Category,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)