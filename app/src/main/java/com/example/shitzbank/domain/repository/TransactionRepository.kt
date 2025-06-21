package com.example.shitzbank.domain.repository

import com.example.shitzbank.domain.model.TransactionResponse

interface TransactionRepository {

    suspend fun getTransactionsForPeriod(
        accountId: Int,
        startDate: String,
        endDate: String
    ): List<TransactionResponse>

}