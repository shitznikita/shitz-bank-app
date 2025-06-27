package com.example.shitzbank.data.dtos

import kotlinx.serialization.Serializable

@Serializable
data class AccountStateDto(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
)
