package com.example.shitzbank.data.dtos

import com.example.shitzbank.common.utils.datetime.parseDateTime
import com.example.shitzbank.domain.model.TransactionRequest
import kotlinx.serialization.EncodeDefault
import kotlinx.serialization.Serializable

@Serializable
data class TransactionRequestDto(
    val accountId: Int,
    val categoryId: Int,
    val amount: String,
    val transactionDate: String,
    @EncodeDefault val comment: String? = null,
) {
    fun toDomain(): TransactionRequest {
        return TransactionRequest(
            accountId = accountId,
            categoryId = categoryId,
            amount = amount.toDouble(),
            transactionDate = parseDateTime(this.transactionDate),
            comment = comment
        )
    }
}
