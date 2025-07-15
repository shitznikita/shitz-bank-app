package com.example.shitzbank.data.dtos

import com.example.shitzbank.common.utils.datetime.parseDateTime
import com.example.shitzbank.domain.model.Transaction
import kotlinx.serialization.Serializable

@Serializable
data class TransactionDto(
    val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
) {
    fun toDomain(): Transaction {
        return Transaction(
            id = this.id,
            accountId = this.accountId,
            categoryId = this.categoryId,
            amount = this.amount,
            transactionDate = parseDateTime(this.transactionDate),
            comment = this.comment,
            createdAt = parseDateTime(this.createdAt),
            updatedAt = parseDateTime(this.updatedAt)
        )
    }
}
