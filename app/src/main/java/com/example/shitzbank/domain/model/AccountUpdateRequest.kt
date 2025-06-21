package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountUpdateRequest(
    val name: String,
    val balance: Double,
    val currency: String
)