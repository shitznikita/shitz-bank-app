package com.example.shitzbank.data.repository

import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.common.utils.datetime.toIsoString
import com.example.shitzbank.data.dtos.TransactionRequestDto
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import kotlinx.coroutines.withContext
import okio.IOException
import java.net.UnknownHostException
import javax.inject.Inject

class TransactionRepositoryImpl
    @Inject
    constructor(
        private val apiService: ShmrFinanceApi,
        private val coroutineDispatchers: CoroutineDispatchers,
    ) : TransactionRepository {
        override suspend fun getTransactionsForPeriod(
            accountId: Int,
            startDate: String,
            endDate: String,
        ): List<TransactionResponse> {
            return withContext(coroutineDispatchers.io) {
                try {
                    val transactionsForPeriodDto =
                        retryWithBackoff {
                            apiService.getTransactionsForPeriod(accountId, startDate, endDate)
                        }
                    transactionsForPeriodDto.map { it.toDomain() }
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                    emptyList()
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                    emptyList()
                }
            }
        }

        override suspend fun createTransaction(
            request: TransactionRequest
        ): TransactionResponse? {
            return withContext(coroutineDispatchers.io) {
                try {
                    val requestDto = TransactionRequestDto(
                        accountId = request.accountId,
                        categoryId = request.categoryId,
                        amount = request.amount,
                        transactionDate = request.transactionDate.toIsoString(),
                        comment = request.comment
                    )
                    val createdTransactionDto =
                        retryWithBackoff {
                            apiService.createTransaction(requestDto)
                        }
                    createdTransactionDto.toDomain()
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                    null
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                    null
                }
            }
        }

        override suspend fun updateTransactionById(
            transactionId: Int,
            request: TransactionRequest
        ) {
            withContext(coroutineDispatchers.io) {
                try {
                    val requestDto = TransactionRequestDto(
                        accountId = request.accountId,
                        categoryId = request.categoryId,
                        amount = request.amount,
                        transactionDate = request.transactionDate.toIsoString(),
                        comment = request.comment
                    )
                    retryWithBackoff {
                        apiService.updateTransactionById(transactionId, requestDto)
                    }
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                }
            }
        }

        override suspend fun getTransactionById(
            transactionId: Int
        ): TransactionResponse? {
            return withContext(coroutineDispatchers.io) {
                try {
                    val transactionResponseDto = retryWithBackoff {
                        apiService.getTransactionById(transactionId)
                    }
                    transactionResponseDto.toDomain()
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                    null
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                    null
                }
            }
        }

        override suspend fun deleteTransactionById(
            transactionId: Int
        ): Boolean {
            return withContext(coroutineDispatchers.io) {
                try {
                    val deleteTransactionResponse = retryWithBackoff {
                        apiService.deleteTransactionById(transactionId)
                    }
                    deleteTransactionResponse
                } catch (e: UnknownHostException) {
                    println("No internet connection or unknown host: ${e.message}")
                    false
                } catch (e: IOException) {
                    println("Network error: ${e.message}")
                    false
                }
            }
        }
    }
