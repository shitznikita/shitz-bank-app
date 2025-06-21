package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?
)