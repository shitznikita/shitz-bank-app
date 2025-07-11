package com.example.shitzbank.domain.usecase.transactions

import com.example.shitzbank.domain.model.TransactionResponse
import com.example.shitzbank.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTransactionByIdUseCase @Inject constructor(
    private val transactionRepository: TransactionRepository
) {

    suspend fun execute(
        transactionId: Int
    ): TransactionResponse {
        return transactionRepository.getTransactionById(transactionId)!!
    }

}