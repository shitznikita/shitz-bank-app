package com.example.shitzbank.data.repository

import com.example.shitzbank.common.CoroutineDispatchers
import com.example.shitzbank.common.network.ConnectionStatus
import com.example.shitzbank.common.network.NetworkMonitor
import com.example.shitzbank.common.network.retryWithBackoff
import com.example.shitzbank.data.dtos.AccountCreateRequestDto
import com.example.shitzbank.data.local.dao.AccountDao
import com.example.shitzbank.data.network.ShmrFinanceApi
import com.example.shitzbank.domain.model.Account
import com.example.shitzbank.domain.model.AccountCreateRequest
import com.example.shitzbank.domain.repository.AccountRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import okio.IOException
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val apiService: ShmrFinanceApi,
    private val coroutineDispatchers: CoroutineDispatchers,
    private val accountDao: AccountDao,
    private val networkMonitor: NetworkMonitor,
) : AccountRepository {
    private suspend fun isNetworkAvailable(): Boolean {
        return networkMonitor.connectionStatus.firstOrNull() is ConnectionStatus.Available
    }

    override suspend fun createAccount(request: AccountCreateRequest): Account {
        return withContext(coroutineDispatchers.io) {
            if (isNetworkAvailable()) {
                try {
                    val requestDto = AccountCreateRequestDto(
                        name = request.name,
                        balance = request.balance,
                        currency = request.currency,
                    )
                    val createAccountResponseDto = retryWithBackoff {
                        apiService.createAccount(requestDto)
                    }
                    val createdAccount = createAccountResponseDto
                    accountDao.insertAccountEntity(createdAccount.toEntity())
                    return@withContext createdAccount.toDomain()
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw IOException("No network available to create account.")
            }
        }
    }

    override suspend fun getAccounts(): List<Account> {
        return withContext(coroutineDispatchers.io) {
            if (isNetworkAvailable()) {
                val accountsDto = retryWithBackoff {
                    apiService.getAccounts()
                }
                accountDao.deleteAllAccounts()
                accountDao.insertAccountsEntities(accountsDto.map { it.toEntity() })
            }
            val accounts = accountDao.getAccountsEntities().map { it.toDomain() }
            accounts
        }
    }

    override suspend fun updateAccount(id: Int, request: AccountCreateRequest): Account {
        return withContext(coroutineDispatchers.io) {
            if (isNetworkAvailable()) {
                try {
                    val requestDto = AccountCreateRequestDto(
                        name = request.name,
                        balance = request.balance,
                        currency = request.currency
                    )
                    val updateAccountResponseDto = retryWithBackoff {
                        apiService.updateAccountById(id, requestDto)
                    }
                    val updatedAccount = updateAccountResponseDto
                    accountDao.insertAccountEntity(updatedAccount.toEntity())
                    return@withContext updatedAccount.toDomain()
                } catch (e: Exception) {
                    throw e
                }
            } else {
                throw IOException("No network available to update account.")
            }
        }
    }
}
