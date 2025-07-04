package com.example.shitzbank.data.dtos

import com.example.shitzbank.common.utils.datetime.parseDateTime
import com.example.shitzbank.domain.model.TransactionResponse
import kotlinx.serialization.Serializable

@Serializable
data class TransactionResponseDto(
    val id: Int,
    val account: AccountBriefDto,
    val category: CategoryDto,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
) {
    fun toDomain(): TransactionResponse {
        return TransactionResponse(
            id = this.id,
            account = this.account.toDomain(),
            category = this.category.toDomain(),
            amount = this.amount,
            transactionDate = parseDateTime(this.transactionDate),
            comment = this.comment,
            createdAt = parseDateTime(this.createdAt),
            updatedAt = parseDateTime(this.updatedAt),
        )
    }
}
