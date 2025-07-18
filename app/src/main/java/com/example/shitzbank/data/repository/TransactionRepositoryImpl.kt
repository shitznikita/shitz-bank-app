package com.example.shitzbank.data.repository

import android.util.Log
import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.common.utils.datetime.parseDateTime
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
import okio.IOException
import java.net.UnknownHostException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

class TransactionRepositoryImpl
    @Inject
    constructor(
        private val apiService: ShmrFinanceApi,
        private val coroutineDispatchers: CoroutineDispatchers,
        private val transactionDao: TransactionDao,
        private val accountDao: AccountDao,
        private val categoryDao: CategoryDao,
        private val networkMonitor: NetworkMonitor,
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
            val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE // Для парсинга YYYY-MM-DD
            val roomStartDate = LocalDate.parse(startDate, dateFormatter).atStartOfDay().toIsoZString()
            val roomEndDate = LocalDate.parse(endDate, dateFormatter).atTime(23, 59, 59, 999_999_999).toIsoZString()

            if (isNetworkAvailable()) {
                try {
                    val transactionsForPeriodDto = retryWithBackoff {
                        apiService.getTransactionsForPeriod(accountId, startDate, endDate)
                    }
                    // Очищаем существующие транзакции для этого аккаунта и периода, затем вставляем новые.
                    // Более точная синхронизация может включать сравнение и обновление по ID.
                    // Для простоты, пока будем вставлять все, полагаясь на REPLACE.
                    // В реальном приложении, возможно, потребуется более детальное удаление/обновление.
                    // Например, сначала удалить все транзакции, связанные с аккаунтом за период,
                    // а затем вставить новые.
                    transactionDao.insertTransactionEntities(transactionsForPeriodDto.map { it.toEntity() })
                    println("Transactions for account $accountId synchronized from API and saved to Room.")
                } catch (e: Exception) { // Ловим все исключения (UnknownHostException, IOException, другие)
                    println("Failed to synchronize transactions from API: ${e.message}. Using cached data.")
                }
            } else {
                println("No network available for getTransactionsForPeriod. Using cached data.")
            }
            // Всегда возвращаем данные из Room.
            // Передаем DAO в метод, так как TransactionDao требует их для сборки TransactionResponse.

            transactionDao.getTransactionsForPeriod(accountId, roomStartDate, roomEndDate, accountDao, categoryDao)
        }
    }

    override suspend fun createTransaction(
        request: TransactionRequest
    ): Transaction? {
        return withContext(coroutineDispatchers.io) {
            // 1. Сначала сохраняем транзакцию в Room с флагом isPendingSync = true
            val localTransaction = transactionDao.createTransaction(request) // DAO создаст Entity с временным ID
            println("Transaction saved to Room (pending sync): ${localTransaction.id}")

            // 2. Затем пытаемся отправить на сервер
            if (isNetworkAvailable()) {
                try {
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

                    // Если успешно создано на сервере, обновляем локальную запись:
                    // - Старый временный ID удаляется.
                    // - Новая запись с реальным ID сервера вставляется (или обновляется, если ID совпали).
                    // - Флаг isPendingSync = false
                    transactionDao.deleteTransactionById(localTransaction.id) // Удаляем временную запись
                    transactionDao.insertTransactionEntity(remoteTransaction.toEntity()) // Вставляем запись с реальным ID
                    println("Transaction successfully created on API and updated in Room with ID: ${remoteTransaction.id}")
                    return@withContext remoteTransaction.toDomain() // Возвращаем реальную транзакцию с сервера
                } catch (e: Exception) {
                    println("Failed to create transaction online: ${e.message}. Keeping as pending sync.")
                    // В случае ошибки, localTransaction останется в Room с isPendingSync = true
                    // SyncWorker должен будет позже отправить ее
                    return@withContext localTransaction // Возвращаем локальную транзакцию с временным ID
                }
            } else {
                println("No network available. Transaction created locally, pending sync.")
                return@withContext localTransaction // Возвращаем локальную транзакцию с временным ID
            }
        }
    }

    override suspend fun updateTransactionById(
        transactionId: Int,
        request: TransactionRequest
    ): TransactionResponse? {
        return withContext(coroutineDispatchers.io) {
            // 1. Сначала обновляем транзакцию в Room с флагом isPendingSync = true
            // transactionDao.updateTransactionById() уже пометит как isPendingSync = true
            val updatedLocalTransactionResponse = transactionDao.updateTransactionById(transactionId, request)
            println("Transaction with ID $transactionId updated in Room (pending sync).")

            // 2. Затем пытаемся отправить на сервер
            if (isNetworkAvailable()) {
                try {
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

                    // Если успешно обновлено на сервере, обновляем флаг isPendingSync = false в Room
                    transactionDao.updateTransactionSyncStatus(transactionId, false)
                    println("Transaction with ID $transactionId successfully updated on API and in Room.")
                    return@withContext remoteTransactionResponse // Возвращаем обновленную транзакцию с сервера
                } catch (e: Exception) {
                    println("Failed to update transaction online: ${e.message}. Keeping as pending sync.")
                    // В случае ошибки, транзакция останется в Room с isPendingSync = true
                    return@withContext updatedLocalTransactionResponse // Возвращаем локальную обновленную транзакцию
                }
            } else {
                println("No network available. Transaction updated locally, pending sync.")
                return@withContext updatedLocalTransactionResponse // Возвращаем локальную обновленную транзакцию
            }
        }
    }

    override suspend fun getTransactionById(
        transactionId: Int
    ): TransactionResponse? {
        return withContext(coroutineDispatchers.io) {
            var transactionFromRoom: TransactionResponse? = null
            try {
                // Сначала пытаемся получить из Room
                transactionFromRoom = transactionDao.getTransactionById(transactionId, accountDao, categoryDao)

                // Если есть сеть, пытаемся обновить из API
                if (isNetworkAvailable()) {
                    val transactionResponseDto = retryWithBackoff {
                        apiService.getTransactionById(transactionId)
                    }
                    val remoteTransactionResponse = transactionResponseDto
                    // Обновляем Room новой версией
                    transactionDao.insertTransactionEntity(remoteTransactionResponse.toEntity()) // Room.REPLACE обновит
                    println("Transaction with ID $transactionId synchronized from API and saved to Room.")
                    return@withContext remoteTransactionResponse.toDomain() // Возвращаем свежую из сети
                } else {
                    println("No network for getTransactionById. Returning cached data.")
                }
            } catch (e: Exception) {
                println("Failed to synchronize single transaction from API: ${e.message}. Returning cached data.")
            }
            return@withContext transactionFromRoom // Возвращаем из Room (может быть null, если не найдено нигде)
        }
    }

    override suspend fun deleteTransactionById(
        transactionId: Int
    ): Boolean {
        return withContext(coroutineDispatchers.io) {
            // 1. Сначала удаляем из Room
            val rowsDeleted = transactionDao.deleteTransactionById(transactionId)
            println("Transaction with ID $transactionId deleted from Room. Rows deleted: $rowsDeleted.")

            // 2. Затем пытаемся удалить на сервере
            if (isNetworkAvailable()) {
                try {
                    val isSuccessful = retryWithBackoff {
                        apiService.deleteTransactionById(transactionId).isSuccessful
                    }
                    if (isSuccessful) {
                        println("Transaction with ID $transactionId successfully deleted on API.")
                        return@withContext true
                    } else {
                        println("Failed to delete transaction on API. Maybe it didn't exist there.")
                        // Если удаление на API не удалось, но локально удалено, это нормально
                        // если API сообщает 404 (нет такой транзакции), но если это 500, то это проблема.
                        // Пока просто возвращаем локальный успех.
                        return@withContext true // Возвращаем true, т.к. локально удалили
                    }
                } catch (e: Exception) {
                    println("Failed to delete transaction online: ${e.message}. Local delete successful.")
                    // В случае ошибки, транзакция останется удаленной локально.
                    // Для более сложного сценария, можно было бы ввести флаг isPendingDelete
                    return@withContext true // Возвращаем true, т.к. локально удалили
                }
            } else {
                println("No network available. Transaction deleted locally. Remote delete pending.")
                // В этом случае, если нет сети, транзакция удаляется локально.
                // Для синхронизации удаления на сервере потребуется WorkManager с флагом isPendingDelete.
                // Пока просто считаем, что локальное удаление является успехом.
                return@withContext true
            }
        }
    }

    // Метод для синхронизации транзакций, ожидающих отправки.
    // Его будет вызывать SyncWorker.
    suspend fun syncPendingTransactions(): List<Transaction> {
        return withContext(coroutineDispatchers.io) {
            val pendingTransactions = transactionDao.getPendingSyncTransactions()
            val successfullySynced = mutableListOf<Transaction>()

            if (!isNetworkAvailable()) {
                println("No network available for pending transactions sync.")
                return@withContext emptyList()
            }

            for (transaction in pendingTransactions) {
                try {
                    val requestDto = TransactionRequestDto(
                        accountId = transaction.accountId,
                        categoryId = transaction.categoryId,
                        amount = String.format(Locale.US, "%.2f", transaction.amount),
                        transactionDate = transaction.transactionDate.toIsoZString(),
                        comment = transaction.comment
                    )

                    // Если ID отрицательный, это новая транзакция
                    if (transaction.id < 0) {
                        val createdDto = retryWithBackoff { apiService.createTransaction(requestDto) }
                        val remoteTransaction = createdDto
                        // Удаляем старую запись с временным ID
                        transactionDao.deleteTransactionById(transaction.id)
                        // Вставляем новую с реальным ID
                        transactionDao.insertTransactionEntity(remoteTransaction.toEntity())
                        successfullySynced.add(remoteTransaction.toDomain())
                        println("Pending new transaction synced with ID: ${remoteTransaction.id}")
                    } else {
                        // Иначе, это существующая транзакция, которую нужно обновить
                        val updatedDto = retryWithBackoff { apiService.updateTransactionById(transaction.id, requestDto) }
                        val remoteTransaction = updatedDto // Dto должен маппиться в TransactionResponse, а не Transaction, проверь свой маппер.
                        // Обновляем статус синхронизации
                        transactionDao.updateTransactionSyncStatus(transaction.id, false)
                        successfullySynced.add(remoteTransaction.toTransactionDomain())
                        println("Pending update transaction synced with ID: ${transaction.id}")
                    }
                } catch (e: Exception) {
                    println("Failed to sync pending transaction with ID ${transaction.id}: ${e.message}")
                    // Оставляем транзакцию с isPendingSync = true для повторной попытки
                }
            }
            successfullySynced
        }
    }
    }
