package com.example.shitzbank.domain.usecase

import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionsUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionResponse> {
        return retryWithBackoff {
            transactionRepository.getTransactionsForPeriod(accountId, startDate, endDate).reversed()
        }
    }

}