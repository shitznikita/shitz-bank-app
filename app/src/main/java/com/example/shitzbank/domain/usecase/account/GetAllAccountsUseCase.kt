package com.example.shitzbank.domain.usecase.account

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

class GetAllAccountsUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(): List<Account> {
        return accountRepository.getAccounts()
    }

}