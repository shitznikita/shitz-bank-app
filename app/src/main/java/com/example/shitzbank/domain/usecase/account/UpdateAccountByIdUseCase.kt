package com.example.shitzbank.domain.usecase.account

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

class UpdateAccountByIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(
        id: Int,
        name: String,
        balance: Double,
        currency: String
    ): Account {
        val request = AccountCreateRequest(name, balance, currency)
        return accountRepository.updateAccount(id, request)
    }

}