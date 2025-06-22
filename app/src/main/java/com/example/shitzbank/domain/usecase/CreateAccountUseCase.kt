package com.example.shitzbank.domain.usecase

import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

class CreateAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(
        name: String,
        balance: Double,
        currency: String
    ): Account {
        val request = AccountCreateRequest(name, balance, currency)
        return retryWithBackoff {
            accountRepository.createAccount(request)
        }
    }

}