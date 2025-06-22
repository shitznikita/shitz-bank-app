package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction (
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String
)
