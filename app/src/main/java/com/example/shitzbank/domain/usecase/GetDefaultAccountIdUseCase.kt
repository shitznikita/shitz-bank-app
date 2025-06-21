package com.example.shitzbank.domain.usecase

import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

class GetDefaultAccountIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(): Int? {
        return try {
            val accounts = accountRepository.getAccounts()
            accounts.firstOrNull()?.id
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}