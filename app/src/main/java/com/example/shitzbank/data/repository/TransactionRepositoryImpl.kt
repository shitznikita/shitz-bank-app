package com.example.shitzbank.data.repository

import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.data.network.ShmrFinanceApi
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
    }
