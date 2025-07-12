package com.example.shitzbank.domain.model

import java.time.LocalDateTime

data class TransactionRequest(
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: LocalDateTime,
    val comment: String?
)