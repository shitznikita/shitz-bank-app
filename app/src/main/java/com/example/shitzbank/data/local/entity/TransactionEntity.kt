package com.example.shitzbank.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.shitzbank.common.utils.datetime.parseDateTime
import com.example.shitzbank.domain.model.AccountBrief
import com.example.shitzbank.domain.model.Category
import com.example.shitzbank.domain.model.Transaction
import com.example.shitzbank.domain.model.TransactionResponse

@Entity(tableName = "transactions",
    foreignKeys = [
        ForeignKey(entity = AccountEntity::class,
            parentColumns = ["id"],
            childColumns = ["accountId"],
            onDelete = ForeignKey.CASCADE),
        ForeignKey(entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class TransactionEntity(
    @PrimaryKey val id: Int,
    val accountId: Int,
    val categoryId: Int,
    val amount: Double,
    val transactionDate: String,
    val comment: String?,
    val createdAt: String,
    val updatedAt: String,
    val isPendingSync: Boolean = true,
    val isDeleted: Boolean = false
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

    fun toDomainResponse(account: AccountBrief, category: Category): TransactionResponse {
        return TransactionResponse(
            id = this.id,
            account = account,
            category = category,
            amount = amount,
            transactionDate = parseDateTime(this.transactionDate),
            comment = this.comment,
            createdAt = parseDateTime(this.createdAt),
            updatedAt = parseDateTime(this.updatedAt)
        )
    }
}