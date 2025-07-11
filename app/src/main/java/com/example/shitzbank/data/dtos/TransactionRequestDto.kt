package com.example.shitzbank.data.dtos

import com.example.shitzbank.common.utils.datetime.parseDateTime
import com.example.shitzbank.domain.model.TransactionRequest
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequestDto(
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
) {
    fun toDomain(): TransactionRequest {
        return TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount,
            transactionDate = parseDateTime(this.transactionDate),
            comment = comment
        )
    }
}
