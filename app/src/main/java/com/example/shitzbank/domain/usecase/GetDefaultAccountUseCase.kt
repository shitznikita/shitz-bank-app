package com.example.shitzbank.domain.usecase

import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

class GetDefaultAccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(): Account? {
        return try {
            val accounts = accountRepository.getAccounts()
            accounts.firstOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}