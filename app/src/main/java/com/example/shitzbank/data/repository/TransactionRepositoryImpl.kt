package com.example.shitzbank.data.repository

import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.SyncDataManager
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.common.utils.datetime.toIsoZString
import com.example.shitzbank.data.dtos.TransactionRequestDto
import com.example.shitzbank.data.local.dao.AccountDao
import com.example.shitzbank.data.local.dao.CategoryDao
import com.example.shitzbank.data.local.dao.TransactionDao
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Transaction
import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val apiService: ShmrFinanceApi,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val transactionDao: TransactionDao,
    private val accountDao: AccountDao,
    private val categoryDao: CategoryDao,
    private val networkMonitor: NetworkMonitor,
    private val syncDataManager: SyncDataManager
) : TransactionRepository {

    private suspend fun isNetworkAvailable(): Boolean {
        return networkMonitor.connectionStatus.firstOrNull() is ConnectionStatus.Available
    }

    override suspend fun getTransactionsForPeriod(
        accountId: Int,
        startDate: String,
        endDate: String,
    ): List<TransactionResponse> {
        return withContext(coroutineDispatchers.io) {
            val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
            val apiStartDate = LocalDate.parse(startDate).format(dateFormatter)
            val apiEndDate = LocalDate.parse(endDate).format(dateFormatter)
            val roomStartDate = LocalDate.parse(startDate, dateFormatter).atStartOfDay().toIsoZString()
            val roomEndDate = LocalDate.parse(endDate, dateFormatter).atTime(23, 59, 59, 999_999_999).toIsoZString()

            if (isNetworkAvailable()) {
                val transactionsForPeriodDto = retryWithBackoff {
                    apiService.getTransactionsForPeriod(accountId, apiStartDate, apiEndDate)
                }
                transactionDao.insertTransactionEntities(transactionsForPeriodDto.map { it.toEntity() })
            }
            transactionDao.getTransactionsForPeriod(accountId, roomStartDate, roomEndDate, accountDao, categoryDao)
        }
    }

    override suspend fun createTransaction(
        request: TransactionRequest
    ): Transaction? {
        return withContext(coroutineDispatchers.io) {
            val localTransaction = transactionDao.createTransaction(request)

            if (isNetworkAvailable()) {
                val requestDto = TransactionRequestDto(
                    accountId = request.accountId,
                    categoryId = request.categoryId,
                    amount = String.format(Locale.US, "%.2f", request.amount),
                    transactionDate = request.transactionDate.toIsoZString(),
                    comment = request.comment
                )
                val createdTransactionDto = retryWithBackoff {
                    apiService.createTransaction(requestDto)
                }
                val remoteTransaction = createdTransactionDto

                transactionDao.deleteTransactionById(localTransaction.id)
                transactionDao.insertTransactionEntity(remoteTransaction.toEntity())
                return@withContext remoteTransaction.toDomain()
            }
            return@withContext localTransaction
        }
    }

    override suspend fun updateTransactionById(
        transactionId: Int,
        request: TransactionRequest
    ): TransactionResponse? {
        return withContext(coroutineDispatchers.io) {
            val updatedLocalTransactionResponse = transactionDao.updateTransactionById(
                transactionId,
                request,
                accountDao,
                categoryDao
            )

            if (isNetworkAvailable()) {
                val requestDto = TransactionRequestDto(
                    accountId = request.accountId,
                    categoryId = request.categoryId,
                    amount = String.format(Locale.US, "%.2f", request.amount),
                    transactionDate = request.transactionDate.toIsoZString(),
                    comment = request.comment
                )
                val updatedTransactionDto = retryWithBackoff {
                    apiService.updateTransactionById(transactionId, requestDto)
                }
                val remoteTransactionResponse = updatedTransactionDto.toDomain()

                transactionDao.updateTransactionSyncStatus(transactionId, false)
                return@withContext remoteTransactionResponse
            }
            return@withContext updatedLocalTransactionResponse
        }
    }

    override suspend fun getTransactionById(
        transactionId: Int
    ): TransactionResponse? {
        return withContext(coroutineDispatchers.io) {
            var transactionFromRoom: TransactionResponse? = null
            transactionFromRoom = transactionDao.getTransactionById(transactionId, accountDao, categoryDao)

            if (isNetworkAvailable()) {
                val transactionResponseDto = retryWithBackoff {
                    apiService.getTransactionById(transactionId)
                }
                val remoteTransactionResponse = transactionResponseDto
                transactionDao.insertTransactionEntity(remoteTransactionResponse.toEntity())
                return@withContext remoteTransactionResponse.toDomain()
            }
            return@withContext transactionFromRoom
        }
    }

    override suspend fun deleteTransactionById(
        transactionId: Int
    ): Boolean {
        return withContext(coroutineDispatchers.io) {
            transactionDao.markTransactionAsDeleted(transactionId)
            if (isNetworkAvailable()) {
                val isSuccessful = retryWithBackoff {
                    apiService.deleteTransactionById(transactionId).isSuccessful
                }
                if (isSuccessful) {
                    transactionDao.deleteTransactionById(transactionId)
                    return@withContext true
                } else {
                    return@withContext true
                }
            } else {
                return@withContext true
            }
        }
    }

    suspend fun syncPendingTransactions(): List<Transaction> {
        return withContext(coroutineDispatchers.io) {
            val pendingTransactions = transactionDao.getPendingSyncTransactionEntities()
            val successfullySynced = mutableListOf<Transaction>()
            val transactionsToPermanentlyDeleteIds = mutableListOf<Int>()

            if (!isNetworkAvailable()) {
                return@withContext emptyList()
            }

            for (transaction in pendingTransactions) {
                if (transaction.isDeleted) {
                    val isSuccesfull = retryWithBackoff {
                        apiService.deleteTransactionById(transaction.id).isSuccessful
                    }
                    if (isSuccesfull) {
                        transactionsToPermanentlyDeleteIds.add(transaction.id)
                    }
                } else {
                    val requestDto = TransactionRequestDto(
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        amount = String.format(Locale.US, "%.2f", transaction.amount),
                        transactionDate = transaction.transactionDate,
                        comment = transaction.comment
                    )

                    if (transaction.id < 0) {
                        val createdDto =
                            retryWithBackoff { apiService.createTransaction(requestDto) }
                        val remoteTransaction = createdDto
                        transactionDao.deleteTransactionById(transaction.id)
                        transactionDao.insertTransactionEntity(remoteTransaction.toEntity())
                        successfullySynced.add(remoteTransaction.toDomain())
                    } else {
                        val updatedDto = retryWithBackoff {
                            apiService.updateTransactionById(
                                transaction.id,
                                requestDto
                            )
                        }
                        val remoteTransaction = updatedDto
                        transactionDao.updateTransactionSyncStatus(transaction.id, false)
                        successfullySynced.add(remoteTransaction.toTransactionDomain())
                    }
                }
            }

            transactionsToPermanentlyDeleteIds.forEach { id ->
                transactionDao.deleteTransactionById(id)
            }

            if (successfullySynced.isNotEmpty() || transactionsToPermanentlyDeleteIds.isNotEmpty()) {
                syncDataManager.saveLastSyncTime(LocalDateTime.now())
            }

            successfullySynced
        }
    }
}
