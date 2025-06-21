package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class AccountState(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String
)