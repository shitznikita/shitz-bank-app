package com.example.shitzbank.domain.usecase.transactions

import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

class DeleteTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(
        transactionId: Int
    ): Boolean {
        return transactionRepository.deleteTransactionById(transactionId)
    }

}