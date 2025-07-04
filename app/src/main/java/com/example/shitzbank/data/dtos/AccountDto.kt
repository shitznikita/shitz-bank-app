package com.example.shitzbank.data.dtos

import com.example.shitzbank.common.utils.datetime.parseDateTime
import com.example.shitzbank.domain.model.Account
import kotlinx.serialization.Serializable

@Serializable
data class AccountDto(
    val id: Int,
    val userId: Int,
    val name: String,
    val balance: Double,
    val currency: String,
    val createdAt: String,
    val updatedAt: String,
) {
    fun toDomain(): Account {
        return Account(
            id = this.id,
            userId = this.userId,
            name = this.name,
            balance = this.balance,
            currency = this.currency,
            createdAt = parseDateTime(this.createdAt),
            updatedAt = parseDateTime(this.updatedAt),
        )
    }
}
