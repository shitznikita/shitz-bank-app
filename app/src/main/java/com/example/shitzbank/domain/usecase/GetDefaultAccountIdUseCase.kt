package com.example.shitzbank.domain.usecase

import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.domain.repository.AccountRepository
import javax.inject.Inject

class GetDefaultAccountIdUseCase @Inject constructor(
    private val accountRepository: AccountRepository
) {

    suspend fun execute(): Int? {
        val accounts = accountRepository.getAccounts()
        return retryWithBackoff {
            accounts.firstOrNull()?.id
        }
    }

}