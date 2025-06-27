package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountCreateRequestDto(
    val name: String,
    val balance: Double,
    val currency: String,
)
