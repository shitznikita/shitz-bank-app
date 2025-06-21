package com.example.shitzbank.domain.usecase

import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

class GetExpensesUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionResponse> {
        val allTransactions = transactionRepository.getTransactionsForPeriod(accountId, startDate, endDate)
        return allTransactions.filter { !it.category.isIncome }
    }

}