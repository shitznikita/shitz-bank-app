package com.example.shitzbank.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.shitzbank.common.utils.datetime.toIsoZString
import com.example.shitzbank.data.dtos.TransactionResponseDto
import com.example.shitzbank.data.local.entity.TransactionEntity
import com.example.shitzbank.domain.model.Transaction
import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.model.TransactionResponse
import java.time.LocalDateTime
import java.util.UUID

internal fun TransactionRequest.toNewEntity(
    isPendingSync: Boolean = true
): TransactionEntity {
    return TransactionEntity(
        id = -(UUID.randomUUID().hashCode() and Integer.MAX_VALUE),
        accountId = this.accountId,
        categoryId = this.categoryId,
        amount = this.amount,
        transactionDate = this.transactionDate.toIsoZString(),
        comment = this.comment,
        createdAt = LocalDateTime.now().toIsoZString(),
        updatedAt = LocalDateTime.now().toIsoZString(),
        isPendingSync = isPendingSync
    )
}

internal fun TransactionRequest.toExistingEntity(
    id: Int,
    createdAt: String,
    isPendingSync: Boolean = true
): TransactionEntity {
    return TransactionEntity(
        id = id,
        accountId = this.accountId,
        categoryId = this.categoryId,
        amount = this.amount,
        transactionDate = this.transactionDate.toIsoZString(),
        comment = this.comment,
        createdAt = createdAt,
        updatedAt = LocalDateTime.now().toIsoZString(),
        isPendingSync = isPendingSync
    )
}

@Dao
interface TransactionDao {

    /**
     * Вспомогательный приватный метод для Room, принимает List<TransactionEntity>
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionEntities(transactions: List<TransactionEntity>)

    /**
     * Вспомогательный приватный метод для Room, принимает TransactionEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionEntity(transaction: TransactionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionResponseDto>) {
        insertTransactionEntities(transactions.map { it.toEntity() })
    }

    /**
     * Вспомогательный приватный метод для Room, для обновления.
     */
    @Update
    suspend fun updateTransactionEntity(transaction: TransactionEntity)

    /**
     * Создает новую транзакцию.
     * Принимает TransactionRequest, генерирует временный ID и сохраняет как PendingSync.
     * Возвращает созданную Transaction (с временным ID).
     */
    suspend fun createTransaction(request: TransactionRequest): Transaction {
        val newEntity = request.toNewEntity()
        insertTransactionEntity(newEntity)
        return newEntity.toDomain()
    }

    /**
     * Получает транзакцию по ее ID.
     * Принимает ID, AccountDao и CategoryDao для получения связанных объектов.
     * Возвращает доменную модель TransactionResponse или null.
     */
    @Query("SELECT * FROM transactions WHERE id = :transactionId AND isDeleted = 0")
    suspend fun getTransactionById(
        transactionId: Int,
        accountDao: AccountDao,
        categoryDao: CategoryDao
    ): TransactionResponse? {
        val transactionEntity = getTransactionEntityById(transactionId) ?: return null
        val account = accountDao.getAccountById(transactionEntity.accountId)
        val category = categoryDao.getCategoryById(transactionEntity.categoryId)

        return transactionEntity.toDomainResponse(account!!.toBrief(), category!!)
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает TransactionEntity
     */
    @Query("SELECT * FROM transactions WHERE id = :transactionId")
    suspend fun getTransactionEntityById(transactionId: Int): TransactionEntity?

    /**
     * Обновляет существующую транзакцию по ID.
     * Принимает ID транзакции и TransactionRequest.
     * Возвращает обновленную TransactionResponse.
     */
    suspend fun updateTransactionById(
        transactionId: Int,
        request: TransactionRequest,
        accountDao: AccountDao,
        categoryDao: CategoryDao
    ): TransactionResponse? {
        val existingEntity = getTransactionEntityById(transactionId)
            ?: return null

        val updatedEntity = request.toExistingEntity(
            id = transactionId,
            createdAt = existingEntity.createdAt,
            isPendingSync = true
        )
        updateTransactionEntity(updatedEntity)
        return getTransactionById(
            transactionId,
            accountDao,
            categoryDao
        )
    }


    /**
     * Получает транзакции по счету за указанный период.
     * Принимает ID счета, startDate, endDate.
     * Возвращает список доменных моделей TransactionResponse.
     */
    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND transactionDate BETWEEN :startDate AND :endDate AND isDeleted = 0")
    suspend fun getTransactionsForPeriod(
        accountId: Int,
        startDate: String,
        endDate: String,
        accountDao: AccountDao,
        categoryDao: CategoryDao
    ): List<TransactionResponse> {
        return getTransactionEntitiesForPeriod(accountId, startDate, endDate).map { entity ->
            val account = accountDao.getAccountById(entity.accountId)
            val category = categoryDao.getCategoryById(entity.categoryId)
            entity.toDomainResponse(account!!.toBrief(), category!!)
        }
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает List<TransactionEntity>
     */
    @Query("SELECT * FROM transactions WHERE accountId = :accountId AND transactionDate BETWEEN :startDate AND :endDate AND isDeleted = 0")
    suspend fun getTransactionEntitiesForPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionEntity>


    /**
     * **Мягкое удаление**: Помечает транзакцию как удаленную и ожидающую синхронизации.
     * Принимает ID транзакции.
     * @return Количество измененных строк.
     */
    @Query("UPDATE transactions SET isDeleted = 1, isPendingSync = 1 WHERE id = :transactionId")
    suspend fun markTransactionAsDeleted(transactionId: Int): Int

    /**
     * Удаляет транзакцию по ее ID.
     * Принимает ID транзакции.
     * Возвращает количество удаленных строк.
     */
    @Query("DELETE FROM transactions WHERE id = :transactionId")
    suspend fun deleteTransactionById(transactionId: Int): Int

    /**
     * Получает все транзакции, которые ожидают синхронизации с сервером.
     * Возвращает список доменных моделей Transaction (без связанных Account/Category).
     */
    @Query("SELECT * FROM transactions WHERE isPendingSync = 1")
    suspend fun getPendingSyncTransactions(): List<Transaction> {
        return getPendingSyncTransactionEntities().map { it.toDomain() }
    }

    /**
     * Вспомогательный приватный метод для Room, возвращает List<TransactionEntity>
     */
    @Query("SELECT * FROM transactions WHERE isPendingSync = 1")
    suspend fun getPendingSyncTransactionEntities(): List<TransactionEntity>

    /**
     * Обновляет флаг isPendingSync для транзакции по ID.
     */
    @Query("UPDATE transactions SET isPendingSync = :isPendingSync WHERE id = :transactionId")
    suspend fun updateTransactionSyncStatus(transactionId: Int, isPendingSync: Boolean)

    /**
     * Удаляет все транзакции из базы данных.
     */
    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()
}