package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountHistoryDto(
    val id: Int,
    val accountId: Int,
    val changeType: String,
    val previousState: AccountStateDto?,
    val newState: AccountStateDto,
    val changeTimestamp: String,
    val createdAt: String,
)
