package com.example.shitzbank.domain.repository

import com.example.shitzbank.data.TransactionResponse
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.Category

interface Repository {
    suspend fun getAccounts(): List<Account>
    suspend fun getCategories(): List<Category>
    suspend fun getTodayTransactionsByAccount(
        accountId: Int,
    ): List<TransactionResponse>
}