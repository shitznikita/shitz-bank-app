package com.example.shitzbank.data.dtos

import com.example.shitzbank.domain.model.AccountBrief
import kotlinx.serialization.Serializable

@Serializable
data class AccountBriefDto(
    val id: Int,
    val name: String,
    val balance: Double,
    val currency: String,
) {
    fun toDomain(): AccountBrief {
        return AccountBrief(
            id = this.id,
            name = this.name,
            balance = this.balance,
            currency = this.currency,
        )
    }
}
