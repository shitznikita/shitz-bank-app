package com.example.shitzbank.domain.repository

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest

interface AccountRepository {

    suspend fun createAccount(request: AccountCreateRequest): Account
    suspend fun getAccounts(): List<Account>

}