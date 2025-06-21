package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryResponse(
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: Double,
    val history: AccountHistory
)