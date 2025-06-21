package com.example.shitzbank.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val createdAt: String,
    val updatedAt: String
)
