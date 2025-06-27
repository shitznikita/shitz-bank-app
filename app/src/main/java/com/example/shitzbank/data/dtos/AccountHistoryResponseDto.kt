package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryResponseDto(
    val accountId: Int,
    val accountName: String,
    val currency: String,
    val currentBalance: Double,
    val history: AccountHistoryDto,
)
