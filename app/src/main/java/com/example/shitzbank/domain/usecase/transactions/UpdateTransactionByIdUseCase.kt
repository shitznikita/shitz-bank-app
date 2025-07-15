package com.example.shitzbank.domain.usecase.transactions

import com.example.shitzbank.domain.model.TransactionRequest
import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

class UpdateTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(
        transactionId: Int,
        request: TransactionRequest
    ): TransactionResponse? {
        return transactionRepository.updateTransactionById(transactionId, request)
    }

}