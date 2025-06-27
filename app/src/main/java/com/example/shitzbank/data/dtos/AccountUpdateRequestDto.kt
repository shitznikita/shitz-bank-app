package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountUpdateRequestDto(
    val name: String,
    val balance: Double,
    val currency: String,
)
