package com.example.shitzbank.domain.usecase.transactions

import com.example.shitzbank.domain.model.Transaction
import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

class CreateTransactionUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(
        request: TransactionRequest
    ): Transaction? {
        return transactionRepository.createTransaction(request)
    }

}